//package com.example.flightsearch
//
//// Example using JUnit 4
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.flightsearch.data.Airport
//import com.example.flightsearch.data.AppContainer
//import com.example.flightsearch.data.Favorite
//import com.example.flightsearch.data.FlightPair
//import com.example.flightsearch.data.airport.AirportsRepository
//import com.example.flightsearch.data.dataStore.SearchTextRepository
//import com.example.flightsearch.data.favorite.FavoriteRepository
//import com.example.flightsearch.ui.FlightViewModel
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.flow.lastOrNull
//import kotlinx.coroutines.runBlocking
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.Mock
//import org.mockito.Mockito.mock
//import org.mockito.Mockito.`when`
//import org.mockito.MockitoAnnotations
//
//@ExperimentalCoroutinesApi
//@RunWith(AndroidJUnit4::class)
//class ViewModelTest {
//    @Mock
//    private lateinit var favoriteRepository: FavoriteRepository
//
//    @Mock
//    private lateinit var flightRepository: AirportsRepository
//
//    private lateinit var viewModel: FlightViewModel
//
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var departureAirport1: Airport
//    private lateinit var departureAirport2: Airport
//    private lateinit var destinationAirport1: Airport
//    private lateinit var destinationAirport2: Airport
//    private lateinit var expectedFlightPairs: List<FlightPair>
//
//    @Before
//    fun setup() {
//        MockitoAnnotations.openMocks(this)
//        flightRepository = mock(AirportsRepository::class.java)
//        favoriteRepository = mock(FavoriteRepository::class.java)
//
//        val mockAppContainer: AppContainer = mock(AppContainer::class.java)
//        `when`(mockAppContainer.airportsRepository).thenReturn(flightRepository)
//        `when`(mockAppContainer.favoriteRepository).thenReturn(favoriteRepository)
//
//        val mockApplicationWrapper: ApplicationWrapper = mock(ApplicationWrapper::class.java)
//        `when`(mockApplicationWrapper.container).thenReturn(mockAppContainer)
//        `when`(mockApplicationWrapper.dataStoreManager).thenReturn(mock(SearchTextRepository::class.java))
//
//        viewModel = FlightViewModel(mockApplicationWrapper)
//    }
//
//    @Test
//    fun testPopulateFavoriteFlightPairs() = runBlocking {
//        val mockFavoriteList = createFakeFavoriteFlow()
//        createFakeAirports()
//
//        // Mock the repository functions
//        `when`(favoriteRepository.getAllFavoriteFlights()).thenReturn(mockFavoriteList)
//
//        // Mock the airport retrieval
//        `when`(flightRepository.getAirportByCode("JFK").lastOrNull()).thenReturn(departureAirport1)
//        `when`(flightRepository.getAirportByCode("CDG").lastOrNull()).thenReturn(destinationAirport1)
//        `when`(flightRepository.getAirportByCode("LHR").lastOrNull()).thenReturn(departureAirport2)
//        `when`(flightRepository.getAirportByCode("SFO").lastOrNull()).thenReturn(destinationAirport2)
//
//        // Call the function to populate
//        viewModel.populateFavoriteFlightPairs()
//
//        // Assert that the list is populated correctly
//        assertEquals(expectedFlightPairs, viewModel.favoriteFlightPair)
//    }
//
//    private fun createFakeFavoriteFlow(): Flow<List<Favorite>> {
//        // Simulated favorite data
//        val favoriteList = listOf(
//            Favorite(1, "JFK", "CDG"),
//            Favorite(2, "LHR", "SFO")
//        )
//
//        // Emit the simulated data as a flow
//        return flowOf(favoriteList)
//    }
//
//    private fun createFakeAirports(){
//        departureAirport1 = Airport(
//            id = 1,
//            iataCode = "JFK",
//            name = "John F. Kennedy International Airport",
//            passengers = 5000
//        )
//
//        destinationAirport1 = Airport(
//            id = 2,
//            iataCode = "LHR",
//            name = "Heathrow Airport",
//            passengers = 6000
//        )
//
//        destinationAirport2 = Airport(
//            id = 3,
//            iataCode = "CDG",
//            name = "Charles de Gaulle Airport",
//            passengers = 5500
//        )
//
//        departureAirport2 = Airport(
//            id = 4,
//            iataCode = "SFO",
//            name = "San Francisco International Airport",
//            passengers = 4800
//        )
//
//        expectedFlightPairs = listOf(
//            FlightPair(departureAirport = departureAirport1, arrivalAirport = destinationAirport1),
//            FlightPair(departureAirport = departureAirport2, arrivalAirport = destinationAirport2),
//        )
//
//    }
//}
