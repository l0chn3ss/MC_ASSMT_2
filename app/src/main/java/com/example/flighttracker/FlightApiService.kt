package com.example.flighttracker.api

import com.example.flighttracker.model.FlightResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlightApiService {
    @GET("flights")
    suspend fun getFlights(
        @Query("access_key") accessKey: String,
        @Query("flight_iata") flightIata: String
    ): FlightResponse
}