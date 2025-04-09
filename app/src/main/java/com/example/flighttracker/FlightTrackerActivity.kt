package com.example.flighttracker

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flighttracker.api.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class FlightTrackerActivity : AppCompatActivity() {

    private lateinit var tvFlightInfo: TextView
    private lateinit var btnStopTracking: Button
    private lateinit var btnBack: Button

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_tracker)

        tvFlightInfo = findViewById(R.id.tvFlightInfo)
        btnStopTracking = findViewById(R.id.btnStopTracking)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        btnStopTracking.setOnClickListener {
            coroutineScope.cancel()
            Toast.makeText(this, "Tracking stopped", Toast.LENGTH_SHORT).show()
        }

        val flightNumber = intent.getStringExtra("flightNumber")
        if (flightNumber.isNullOrEmpty()) {
            Toast.makeText(this, "Flight number missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchFlightDetails(flightNumber)
    }

    private fun fetchFlightDetails(flightNumber: String) {
        coroutineScope.launch {
            try {
                val apiService = ApiClient.getApiService()
                val response = apiService.getFlights("8622bcf3a7036dd9bc2a574b2264e10c", flightNumber)
                if (response.data.isNotEmpty()) {
                    val flightData = response.data[0]
                    val departureTime = flightData.departure.scheduled ?: "N/A"
                    val arrivalTime = flightData.arrival.scheduled ?: "N/A"
                    val latitude = flightData.live?.latitude?.toString() ?: "N/A"
                    val longitude = flightData.live?.longitude?.toString() ?: "N/A"

                    val infoText = """
                        Flight: $flightNumber
                        Departure: $departureTime
                        Arrival: $arrivalTime
                        Location: $latitude, $longitude
                    """.trimIndent()
                    tvFlightInfo.text = infoText
                } else {
                    tvFlightInfo.text = "No data found for flight $flightNumber"
                }
            } catch (e: Exception) {
                tvFlightInfo.text = "Error fetching flight data: ${e.message}"
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}