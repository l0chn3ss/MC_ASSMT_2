package com.example.flighttracker.data

data class AverageFlightData(
    val iataCode: String,
    val origin: String,
    val destination: String,
    val avgDuration: Double
)