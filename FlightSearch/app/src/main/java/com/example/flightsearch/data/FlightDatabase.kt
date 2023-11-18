package com.example.flightsearch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.flightsearch.data.airport.AirportDao
import com.example.flightsearch.data.favorite.FavoriteDao
import java.io.FileOutputStream

@Database(entities = [Airport::class, Favorite::class], version = 2, exportSchema = false)
abstract class FlightDatabase: RoomDatabase() {
    abstract fun airportDao(): AirportDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: FlightDatabase? = null
        private const val DATABASE_NAME = "flight_database.db"
        private const val PREDEFINED_DATABASE_PATH = "database/flight_search.db"

        // Populate the Airport table from the predefined database
        private fun populateAirportTable(context: Context, db: FlightDatabase) {
            val inputStream = context.assets.open(PREDEFINED_DATABASE_PATH)
            val outputStream = FileOutputStream(context.getDatabasePath(DATABASE_NAME))

            inputStream.copyTo(outputStream)

            inputStream.close()
            outputStream.close()
        }

        fun getDatabase(context: Context): FlightDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlightDatabase::class.java,
                    DATABASE_NAME
                )
                    .createFromAsset(PREDEFINED_DATABASE_PATH)
                    .addCallback(object : Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            populateAirportTable(context, getDatabase(context))
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}