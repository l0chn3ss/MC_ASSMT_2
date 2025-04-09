package com.example.flighttracker.worker

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.Data
import com.example.flighttracker.data.FlightDatabase
import com.example.flighttracker.model.Flight
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

class AverageFlightTimeWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Get the Room database and DAO.
            val db = FlightDatabase.getDatabase(applicationContext)
            val dao = db.flightDao()

            // If the database is empty, insert the hardcoded JSON data.
            if (dao.getAllFlights().isEmpty()) {
                val jsonString = """
                    [
                      {
                        "id": "AIC678-2025-04-08",
                        "aircraft": "A20N",
                        "origin": "Indira Gandhi Int'l (DEL / VIDP)",
                        "destination": "Chatrapati Shivaji Int'l (BOM / VABB)",
                        "departure": "08:54AM IST",
                        "arrival": "10:34AM IST",
                        "duration_minutes": 99,
                        "date": "2025-04-08"
                      },
                      {
                        "id": "AIC678-2025-04-06",
                        "aircraft": "A20N",
                        "origin": "Indira Gandhi Int'l (DEL / VIDP)",
                        "destination": "Chatrapati Shivaji Int'l (BOM / VABB)",
                        "departure": "08:48AM IST",
                        "arrival": "10:45AM IST",
                        "duration_minutes": 117,
                        "date": "2025-04-06"
                      },
                      {
                        "id": "AIC678-2025-04-05",
                        "aircraft": "A20N",
                        "origin": "Indira Gandhi Int'l (DEL / VIDP)",
                        "destination": "Chatrapati Shivaji Int'l (BOM / VABB)",
                        "departure": "08:50AM IST",
                        "arrival": "10:43AM IST",
                        "duration_minutes": 112,
                        "date": "2025-04-05"
                      },
                      {
                        "id": "AIC678-2025-04-03",
                        "aircraft": "A20N",
                        "origin": "Indira Gandhi Int'l (DEL / VIDP)",
                        "destination": "Chatrapati Shivaji Int'l (BOM / VABB)",
                        "departure": "08:58AM IST",
                        "arrival": "11:09AM IST",
                        "duration_minutes": 131,
                        "date": "2025-04-03"
                      },
                      {
                        "id": "AIC678-2025-04-02",
                        "aircraft": "A20N",
                        "origin": "Indira Gandhi Int'l (DEL / VIDP)",
                        "destination": "Chatrapati Shivaji Int'l (BOM / VABB)",
                        "departure": "09:06AM IST",
                        "arrival": "11:08AM IST",
                        "duration_minutes": 121,
                        "date": "2025-04-02"
                      },
                      {
                        "id": "AIC678-2025-04-01",
                        "aircraft": "A320",
                        "origin": "Indira Gandhi Int'l (DEL / VIDP)",
                        "destination": "Chatrapati Shivaji Int'l (BOM / VABB)",
                        "departure": "08:59AM IST",
                        "arrival": "11:08AM IST",
                        "duration_minutes": 128,
                        "date": "2025-04-01"
                      },
                      {
                        "id": "AIC678-2025-03-31",
                        "aircraft": "A320",
                        "origin": "Indira Gandhi Int'l (DEL / VIDP)",
                        "destination": "Chatrapati Shivaji Int'l (BOM / VABB)",
                        "departure": "08:41AM IST",
                        "arrival": "10:28AM IST",
                        "duration_minutes": 106,
                        "date": "2025-03-31"
                      },
                      {
                        "id": "AIC678-2025-03-30",
                        "aircraft": "A320",
                        "origin": "Indira Gandhi Int'l (DEL / VIDP)",
                        "destination": "Chatrapati Shivaji Int'l (BOM / VABB)",
                        "departure": "08:46AM IST",
                        "arrival": "10:41AM IST",
                        "duration_minutes": 115,
                        "date": "2025-03-30"
                      },
                      {
                        "id": "AIC678-2025-03-29",
                        "aircraft": "A20N",
                        "origin": "Indira Gandhi Int'l (DEL / VIDP)",
                        "destination": "Chatrapati Shivaji Int'l (BOM / VABB)",
                        "departure": "08:54AM IST",
                        "arrival": "10:40AM IST",
                        "duration_minutes": 105,
                        "date": "2025-03-29"
                      },
                      {
                        "id": "AIC678-2025-03-28",
                        "aircraft": "A20N",
                        "origin": "Indira Gandhi Int'l (DEL / VIDP)",
                        "destination": "Chatrapati Shivaji Int'l (BOM / VABB)",
                        "departure": "08:42AM IST",
                        "arrival": "10:27AM IST",
                        "duration_minutes": 104,
                        "date": "2025-03-28"
                      },
                      {
                        "id": "AIC678-2025-03-27",
                        "aircraft": "A20N",
                        "origin": "Indira Gandhi Int'l (DEL / VIDP)",
                        "destination": "Chatrapati Shivaji Int'l (BOM / VABB)",
                        "departure": "09:16AM IST",
                        "arrival": "11:21AM IST",
                        "duration_minutes": 124,
                        "date": "2025-03-27"
                      },
                      {
                        "id": "AIC678-2025-03-26",
                        "aircraft": "A20N",
                        "origin": "Indira Gandhi Int'l (DEL / VIDP)",
                        "destination": "Chatrapati Shivaji Int'l (BOM / VABB)",
                        "departure": "08:43AM IST",
                        "arrival": "10:24AM IST",
                        "duration_minutes": 101,
                        "date": "2025-03-26"
                      }
                    ]
                """.trimIndent()

                val moshi = Moshi.Builder().build()
                val type = Types.newParameterizedType(List::class.java, Flight::class.java)
                val adapter = moshi.adapter<List<Flight>>(type)
                val flightsJson: List<Flight>? = adapter.fromJson(jsonString)
                if (flightsJson != null) {
                    dao.insertAll(flightsJson)
                }
            }

            // Retrieve all flight records from the database.
            val flights = dao.getAllFlights()

            var totalMinutes = 0L
            var count = 0

            // Use a formatter for strings like "08:54AM"
            val timeFormatter = DateTimeFormatter.ofPattern("hh:mma", Locale.US)

            // For each flight, compute the duration in minutes.
            for (flight in flights) {
                try {
                    // Remove trailing " IST" and trim any extra spaces.
                    val depTimeStr = flight.departure.replace(" IST", "").trim()
                    val arrTimeStr = flight.arrival.replace(" IST", "").trim()

                    // Parse the departure and arrival times.
                    val departureTime = LocalTime.parse(depTimeStr, timeFormatter)
                    val arrivalTime = LocalTime.parse(arrTimeStr, timeFormatter)

                    // Parse the flight date (the JSON date format "yyyy-MM-dd" is ISO compliant).
                    val flightDate = LocalDate.parse(flight.date)

                    val departureDateTime = LocalDateTime.of(flightDate, departureTime)
                    var arrivalDateTime = LocalDateTime.of(flightDate, arrivalTime)

                    // If the arrival occurs before the departure, assume the flight ended the next day.
                    if (arrivalDateTime.isBefore(departureDateTime)) {
                        arrivalDateTime = arrivalDateTime.plusDays(1)
                    }

                    val diff = ChronoUnit.MINUTES.between(departureDateTime, arrivalDateTime)
                    totalMinutes += diff
                    count++
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            // Compute the average duration.
            val averageDuration: Double = if (count > 0) totalMinutes.toDouble() / count else 0.0

            // Return the computed average in the output Data.
            val output = Data.Builder().putDouble("averageDuration", averageDuration).build()
            return@withContext Result.success(output)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return@withContext Result.failure()
        }
    }
}