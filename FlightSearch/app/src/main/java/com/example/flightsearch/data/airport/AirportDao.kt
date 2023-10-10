package com.example.flightsearch.data.airport

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flightsearch.data.Airport
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(airport: Airport)

    @Query("SELECT * from airport")
    fun getAirports() : Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE iata_code = :airportCode")
    fun getAirportByCode(airportCode: String): Flow<Airport>

    @Query("SELECT * FROM airport WHERE name LIKE :query OR iata_code LIKE :query")
    fun filterAirports(query: String): Flow<List<Airport>>
}