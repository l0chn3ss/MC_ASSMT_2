package com.example.flighttracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Flight input and tracking button
        val etFlightNumber = findViewById<EditText>(R.id.etFlightNumber)
        val btnTrackFlight = findViewById<Button>(R.id.btnTrackFlight)
        btnTrackFlight.setOnClickListener {
            val flightNumber = etFlightNumber.text.toString().trim()
            if (flightNumber.isEmpty()) {
                etFlightNumber.error = "Enter flight IATA code"
                return@setOnClickListener
            }
            val intent = Intent(this, FlightTrackerActivity::class.java)
            intent.putExtra("flightNumber", flightNumber)
            startActivity(intent)
        }

        // Average Flight Time button
        val btnAvgFlightTime = findViewById<Button>(R.id.btnAvgFlightTime)
        btnAvgFlightTime.setOnClickListener {
            startActivity(Intent(this, AverageTimeActivity::class.java))
        }
    }
}