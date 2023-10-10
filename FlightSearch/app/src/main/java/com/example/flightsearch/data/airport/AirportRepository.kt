package com.example.flightsearch.data.airport

import com.example.flightsearch.data.Airport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

interface AirportsRepository {
    /** Retrieves list of airports from underlying data source */
    fun getAirports(): Flow<List<Airport>>

    /** Retrieve an airport from the given data source  */
    suspend fun getAirportByCode(code: String) : Flow<Airport>

    /** Insert airport from data source */
    suspend fun insertAirport(airport: Airport)

    suspend fun filterAirports(query: String) : List<Airport>
}

class DefaultAirportsRepository(
    private val airportDao: AirportDao
) : AirportsRepository {
    override fun getAirports(): Flow<List<Airport>> =
        airportDao.getAirports()

    override suspend fun getAirportByCode(code: String): Flow<Airport> =
        airportDao.getAirportByCode(code)

    override suspend fun insertAirport(airport: Airport) =
        airportDao.insert(airport)

    override suspend fun filterAirports(query: String): List<Airport> =
        airportDao.filterAirports(query).firstOrNull() ?: emptyList()
}