package com.example.flighttracker

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.flighttracker.worker.AverageFlightTimeWorker

class AverageTimeActivity : AppCompatActivity() {

    private lateinit var tvAverageTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_average_time)

        tvAverageTime = findViewById(R.id.tvAverageTime)

        val workRequest = OneTimeWorkRequestBuilder<AverageFlightTimeWorker>().build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)

        WorkManager.getInstance(applicationContext)
            .getWorkInfoByIdLiveData(workRequest.id)
            .observe(this) { info ->
                if (info != null && info.state.isFinished) {
                    val avg = info.outputData.getDouble("averageDuration", 0.0)
                    tvAverageTime.text = "Average Flight Duration: ${"%.2f".format(avg)} minutes"
                }
            }
    }
}