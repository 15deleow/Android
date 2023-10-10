package com.example.flightsearch.data.favorite

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flightsearch.data.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteFlight(favorite: Favorite)

    @Delete
    suspend fun deleteFavoriteFlight(favorite: Favorite)

    @Query("SELECT * FROM favorite")
    fun getAllFavoriteFlights() : Flow<List<Favorite>>

    @Query("SELECT * FROM favorite WHERE departure_code = :departureCode AND destination_code = :destinationCode")
    suspend fun getFavoriteByFlightPair(departureCode: String, destinationCode: String): Favorite?
}