package com.example.flightsearch.data

import android.content.Context
import com.example.flightsearch.data.airport.AirportsRepository
import com.example.flightsearch.data.airport.DefaultAirportsRepository
import com.example.flightsearch.data.favorite.DefaultFavoriteRepository
import com.example.flightsearch.data.favorite.FavoriteRepository

class AppDataContainer(private val context: Context) : AppContainer {
    private val airportDatabase = FlightDatabase.getDatabase(context)

    override val airportsRepository: AirportsRepository by lazy {
        DefaultAirportsRepository(airportDatabase.airportDao())
    }

    override val favoriteRepository: FavoriteRepository by lazy {
        DefaultFavoriteRepository(FlightDatabase.getDatabase(context).favoriteDao())
    }
}