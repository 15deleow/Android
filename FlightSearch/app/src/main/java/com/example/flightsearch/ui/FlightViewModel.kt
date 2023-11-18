package com.example.flightsearch.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FlightSearchApplication
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FlightPair
import com.example.flightsearch.data.airport.AirportsRepository
import com.example.flightsearch.data.dataStore.SearchTextRepository
import com.example.flightsearch.data.favorite.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

private val TAG = "VIEWMODEL"

data class FlightUiState(
    var flightList: List<FlightPair> = mutableListOf(),
    val searchText: String = "",
)

class FlightViewModel(
    application: FlightSearchApplication,
): ViewModel() {
    private val searchTextRepository: SearchTextRepository = application.searchTextRepository
    private val flightRepository: AirportsRepository = application.container.airportsRepository
    private val favoriteRepository: FavoriteRepository = application.container.favoriteRepository

    private val _flightUiState: MutableStateFlow<FlightUiState> = MutableStateFlow(FlightUiState())
    val flightUiState: StateFlow<FlightUiState> get() = _flightUiState

    private var _favoriteFlightPairs = MutableStateFlow<List<FlightPair>>(emptyList())
    val favoriteFlightPair: StateFlow<List<FlightPair>> = _favoriteFlightPairs.asStateFlow()

    init {
        viewModelScope.launch {
            //Get user's last search input
            searchTextRepository.saveSearchText.collect{ savedSearchText ->
                _flightUiState.value = _flightUiState.value.copy(searchText = savedSearchText ?: "")
            }
        }

        viewModelScope.launch {
            //Get latest Favorite List
            populateFavoriteFlightPairs()
        }
    }

    fun getAllAirports(): Flow<List<Airport>> = flightRepository.getAirports()

    fun onSearchTextChanged(searchText: String){
        saveSearchText(searchText)
    }

    fun autocompleteSuggestions(query: String, airports: List<Airport>): List<AnnotatedString> {
        val suggestions = mutableListOf<AnnotatedString>()

        //Filter airports that match the query either by name or IATA code
        val filteredAirports = airports.filter {
            it.name.contains(query, ignoreCase = true) || it.iataCode.contains(query, ignoreCase = true)
        }

        //Extract airport names for suggestions
        for(airport in filteredAirports){
            val suggestion = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)){append(airport.iataCode)}
                append(" ${airport.name}")
            }
            suggestions.add(suggestion)
        }

        return suggestions
    }

    fun generateFlights(selectedAirportCode: String, airports: List<Airport>){
        val selectedAirport = airports.find { it.iataCode == selectedAirportCode }
        val flights = mutableListOf<FlightPair>()

        for(airport in airports){
            if(airport != selectedAirport){
                val flightPair = FlightPair(selectedAirport!!, airport)
                flights.add(flightPair)
            }
        }

        setFlightList(flights)
    }

    fun toggleFavorite(flight: FlightPair, isFavorite: Boolean){
        if(isFavorite){
            removeFavoriteFlight(flight)
        }else{
            insertFavoriteFlight(flight)
        }
    }

    /** Helper Functions */
    private fun saveSearchText(searchText: String?){
        viewModelScope.launch {
            searchText?.let {
                searchTextRepository.saveSearchTextPreference(it)
            }
        }
    }

    private fun updateFlightUiState(newFlightUiState: FlightUiState) {
        _flightUiState.value = newFlightUiState
    }

    private fun setFlightList(flights: List<FlightPair>){
        val updatedUiState = flightUiState.value.copy(flightList = flights)
        updateFlightUiState(updatedUiState)
    }

    private fun insertFavoriteFlight(flight: FlightPair){
        //Update local Favorites List
        _favoriteFlightPairs.value = _favoriteFlightPairs.value + flight

        //Add entry to Favorite
        viewModelScope.launch {
            favoriteRepository.insertFavoriteFlight(convertToFavorite(flight))
        }
    }

    private fun removeFavoriteFlight(flight: FlightPair){
        //Update the local favorite list by filtering out the removed entry
        _favoriteFlightPairs.value = _favoriteFlightPairs.value.filterNot { it.matches(flight) }

        viewModelScope.launch {
            //Retrieve the favorite entry that needs to be deleted
            val favoriteToDelete = favoriteRepository.getFavoriteByFlightPair(
                flight.departureAirport.iataCode,
                flight.arrivalAirport.iataCode
            )

            //Delete entry from database
            if (favoriteToDelete != null) {
                favoriteRepository.deleteFavoriteFlight(favoriteToDelete)
            }
        }
    }

    private fun convertToFavorite(flight: FlightPair): Favorite {
        return Favorite(
            id = 0,
            departureCode = flight.departureAirport.iataCode,
            destinationCode = flight.arrivalAirport.iataCode
        )
    }

    private fun FlightPair.matches(favorite: FlightPair): Boolean {
        return this.arrivalAirport.iataCode == favorite.arrivalAirport.iataCode &&
            this.departureAirport.iataCode == favorite.departureAirport.iataCode
    }

    private suspend fun populateFavoriteFlightPairs() {
        //Get list of Favorite flights from database in List format
        val currentList = favoriteRepository.getAllFavoriteFlights().toList()

        currentList.forEach { flight ->
            val departureAirport =
                flightRepository.getAirportByCode(flight.departureCode).firstOrNull()
            val destinationAirport =
                flightRepository.getAirportByCode(flight.destinationCode).firstOrNull()

            if (departureAirport != null && destinationAirport != null) {
                val flightPair = FlightPair(departureAirport, destinationAirport)
                _favoriteFlightPairs.value = _favoriteFlightPairs.value + flightPair
            }
        }
    }

    private suspend fun Flow<List<Favorite>>.toList(): List<Favorite> {
        return this.firstOrNull() ?: emptyList()
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FlightSearchApplication)
                FlightViewModel(
                    application
                )
            }
        }
    }
}
