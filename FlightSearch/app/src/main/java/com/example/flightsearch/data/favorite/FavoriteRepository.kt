package com.example.flightsearch.data.favorite

import com.example.flightsearch.data.Favorite
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    /** Inserts a Favorite Flight to the database */
    suspend fun insertFavoriteFlight(favorite: Favorite)

    /** Deletes a Favorite Flight from the database */
    suspend fun deleteFavoriteFlight(favorite: Favorite)

    /** Retrieves all Favorite Flights from the database */
    fun getAllFavoriteFlights() : Flow<List<Favorite>>

    /** Get a favorite entry from the database using a FlightPair object **/
    suspend fun getFavoriteByFlightPair(departureCode: String, destinationCode: String): Favorite?
}

class DefaultFavoriteRepository(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {
    override suspend fun insertFavoriteFlight(favorite: Favorite) =
        favoriteDao.insertFavoriteFlight(favorite)

    override suspend fun deleteFavoriteFlight(favorite: Favorite) =
        favoriteDao.deleteFavoriteFlight(favorite)

    override suspend fun getFavoriteByFlightPair(departureCode: String, destinationCode: String) =
        favoriteDao.getFavoriteByFlightPair(departureCode, destinationCode)

    override fun getAllFavoriteFlights(): Flow<List<Favorite>> =
        favoriteDao.getAllFavoriteFlights()
}