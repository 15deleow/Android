package com.example.flightsearch

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.FlightDatabase
import com.example.flightsearch.data.airport.AirportDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AirportDaoTest {
    private lateinit var airportDao: AirportDao
    private lateinit var myDatabase: FlightDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        myDatabase = Room.inMemoryDatabaseBuilder(context, FlightDatabase::class.java).build()
        airportDao = myDatabase.airportDao()
    }

    @After
    fun tearDown() {
        myDatabase.close()
    }

    @Test
    fun testGetAll() = runBlocking{
        val myEntity = Airport(
            0,
            "FCO",
            "Leonardo da Vinci International Airport",
            11662842
        )

        airportDao.getAirports()
            .flowOn(Dispatchers.IO)
            .collect{
                Assert.assertEquals(myEntity, it[0])
            }
    }
}