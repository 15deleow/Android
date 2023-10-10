package com.example.flightsearch.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.ui.FlightViewModel
import com.example.flightsearch.ui.theme.FlightSearchTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightApp(
    flightViewModel: FlightViewModel = viewModel(
        factory = FlightViewModel.Factory
    )
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FlightAppTopAppBar(scrollBehavior = scrollBehavior)
        },
    ) { innerPadding ->
        Column(
        ) {
            FlightAppBody(
                airportViewModel = flightViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun FlightAppBody(
    airportViewModel: FlightViewModel,
    modifier: Modifier = Modifier
){
    val flightUiState by airportViewModel.flightUiState.collectAsState()
    val airportsList by airportViewModel.getAllAirports().collectAsState(initial = emptyList())
    var selectedAirport by remember { mutableStateOf<String>("") }

    Column(modifier = modifier) {
        SearchBar(
            flightUiState = flightUiState,
            autocompleteSuggestions =
                airportViewModel.autocompleteSuggestions(flightUiState.searchText, airportsList),
            onSearchTextChange = { newText ->
                airportViewModel.onSearchTextChanged(newText)
             },
            onSuggestionClick = { selectedText ->
                airportViewModel.onSuggestionClick(selectedText)
                airportViewModel.generateFlights(selectedText, airportsList)
                selectedAirport = selectedText
            }
        )

        if(flightUiState.searchText.isEmpty()){
            FlightCardList(
                flights = airportViewModel.favoriteFlightPair,
                flightViewModel = airportViewModel
            )
        }else{
            if(selectedAirport.isNotEmpty()) {
                FlightCardList(
                    modifier = Modifier,
                    flightUiState.flightList,
                    flightViewModel = airportViewModel
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp(){
    FlightSearchTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            FlightApp()
        }
    }
}