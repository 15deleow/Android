package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {

    @Insert
    suspend fun insertFavoriteRoute(route: Favorite)

    fun getAllFavoriteRoutes(): Flow<List<Favorite>>

    fun getFlights(): Flow<Airport>

    fun searchAirports(query: String): Flow<List<Airport>>

    fun getFlightsForAirport(airportCode: String): List<Airport>
}