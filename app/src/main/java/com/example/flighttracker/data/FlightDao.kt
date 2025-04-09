package com.example.flighttracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flighttracker.model.Flight

@Dao
interface FlightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(flights: List<Flight>)

    @Query("SELECT AVG(duration_minutes) FROM flights")
    suspend fun getAverageDuration(): Double?

    @Query("SELECT * FROM flights")
    suspend fun getAllFlights(): List<Flight>
}