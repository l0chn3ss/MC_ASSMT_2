package com.example.flighttracker.model

import com.squareup.moshi.Json

data class FlightResponse(
    @Json(name = "data")
    val data: List<FlightData>
)

data class FlightData(
    @Json(name = "departure")
    val departure: Departure,
    @Json(name = "arrival")
    val arrival: Arrival,
    @Json(name = "live")
    val live: Live?
)

data class Departure(
    @Json(name = "scheduled")
    val scheduled: String?
)

data class Arrival(
    @Json(name = "scheduled")
    val scheduled: String?
)

data class Live(
    @Json(name = "latitude")
    val latitude: Double?,
    @Json(name = "longitude")
    val longitude: Double?
    // Additional fields can be added as needed.
)