package com.example.flightsearch.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "airport")
data class Airport(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "iata_code")
    val iataCode : String,
    val name: String,
    val passengers: Int
)

@Entity(tableName = "favorite")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "departure_code")
    val departureCode: String,
    @ColumnInfo(name = "destination_code")
    val destinationCode: String
)

data class FlightPair(
    val departureAirport: Airport,
    val arrivalAirport: Airport
)