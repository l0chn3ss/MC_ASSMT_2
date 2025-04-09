package com.example.flighttracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flights")
data class Flight(
    @PrimaryKey val id: String,
    val aircraft: String,
    val origin: String,
    val destination: String,
    val departure: String,
    val arrival: String,
    val duration_minutes: Int,
    val date: String
)