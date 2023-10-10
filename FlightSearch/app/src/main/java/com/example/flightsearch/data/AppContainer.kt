package com.example.flightsearch.data

import com.example.flightsearch.data.airport.AirportsRepository
import com.example.flightsearch.data.favorite.FavoriteRepository

interface AppContainer {
    val airportsRepository : AirportsRepository
    val favoriteRepository : FavoriteRepository
}