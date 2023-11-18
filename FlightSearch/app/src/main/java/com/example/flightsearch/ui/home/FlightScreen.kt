package com.example.flightsearch.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flightsearch.FlightSearchApplication
import com.example.flightsearch.R
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.FlightPair
import com.example.flightsearch.ui.FlightViewModel

@Composable
fun FlightCardList(
    modifier: Modifier = Modifier,
    flights: List<FlightPair>,
    flightViewModel: FlightViewModel
){
    LazyColumn(modifier = modifier) {
        items(items = flights) { flight ->
            FlightCardItem(
                flight = flight,
                flightViewModel = flightViewModel
            )
        }
    }
}

@Composable
fun FlightCardItem(
    modifier: Modifier = Modifier,
    flight: FlightPair,
    flightViewModel: FlightViewModel
    ){
    Card(
        modifier = modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            FlightInfo(flight = flight)
            ToggleFavoriteButton(flightViewModel = flightViewModel, flight = flight)
        }
    }
}

@Composable
fun FlightInfo(flight: FlightPair, modifier: Modifier = Modifier){
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.depart),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodySmall
        )
        FlightRowFormat(
            code = flight.departureAirport.iataCode,
            name = flight.departureAirport.name
        )
        Text(
            text = stringResource(id = R.string.arrive),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodySmall
        )
        FlightRowFormat(
            code = flight.arrivalAirport.iataCode,
            name = flight.arrivalAirport.name,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun FlightRowFormat(code: String, name: String, modifier: Modifier = Modifier){
    Row(modifier = modifier) {
        Text(
            text = code,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun ToggleFavoriteButton(
    flightViewModel: FlightViewModel,
    flight: FlightPair,
    modifier: Modifier = Modifier
){
    // Observe the StateFlow in the UI layer
    val favoriteList by flightViewModel.favoriteFlightPair.collectAsState()
    val isFavorite = favoriteList.contains(flight)

    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            flightViewModel.toggleFavorite(flight, isFavorite)
        }
    ) {
        Icon(
            tint =  Color(0xffE91E63),
            modifier = modifier.graphicsLayer {
                scaleX = 1.3f
                scaleY = 1.3f
            },
            imageVector = if (isFavorite) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewToggleButton(){
    val flights = FlightPair(
        departureAirport = Airport(
            id =  1,
            iataCode = "FCO",
            name = "Leonardo da Vinci International Airport",
            passengers = 100),
        arrivalAirport = Airport(
            id =  2,
            iataCode = "SVO",
            name = "Sheremetyevo- A.S Pushkin International",
            passengers = 100)
    )
    ToggleFavoriteButton(
        flightViewModel = FlightViewModel(FlightSearchApplication()),
        flight = flights
        )
}

@Preview(showBackground = true)
@Composable
fun PreviewFlightCardItem(){
    val flights = FlightPair(
        departureAirport = Airport(
            id =  1,
            iataCode = "FCO",
            name = "Leonardo da Vinci International Airport",
            passengers = 100),
        arrivalAirport = Airport(
            id =  2,
            iataCode = "SVO",
            name = "Sheremetyevo- A.S Pushkin International",
            passengers = 100)
    )
    FlightCardItem(
        flight = flights,
        flightViewModel = FlightViewModel(FlightSearchApplication())
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewFlightList(){
    val flightList = listOf(
        FlightPair(
            departureAirport = Airport(
                id =  1,
                iataCode = "FCO",
                name = "Leonardo da Vinci International Airport",
                passengers = 100),
            arrivalAirport = Airport(
                id =  2,
                iataCode = "SVO",
                name = "Sheremetyevo- A.S Pushkin International",
                passengers = 100)
        ),
        FlightPair(
            departureAirport = Airport(
                id =  1,
                iataCode = "FCO",
                name = "Leonardo da Vinci International Airport",
                passengers = 100),
            arrivalAirport = Airport(
                id =  2,
                iataCode = "SVO",
                name = "Sheremetyevo- A.S Pushkin International",
                passengers = 100)
        ),
        FlightPair(
            departureAirport = Airport(
                id =  1,
                iataCode = "FCO",
                name = "Leonardo da Vinci International Airport",
                passengers = 100),
            arrivalAirport = Airport(
                id =  2,
                iataCode = "SVO",
                name = "Sheremetyevo- A.S Pushkin International",
                passengers = 100)
        )
    )

    FlightCardList(
        flights = flightList,
        flightViewModel = FlightViewModel(FlightSearchApplication())
    )
}
