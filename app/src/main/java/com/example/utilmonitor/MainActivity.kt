package com.example.utilmonitor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.BLACK

        val btnElectricity = findViewById<Button>(R.id.btnElectricity)
        val btnWater = findViewById<Button>(R.id.btnWater)
        val btnGas = findViewById<Button>(R.id.btnGas)
        val btnHistory = findViewById<Button>(R.id.btnHistory)

        btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        btnElectricity.setOnClickListener {
            startActivity(Intent(this, ElectricityActivity::class.java))
        }
        btnWater.setOnClickListener {
            startActivity(Intent(this, WaterActivity::class.java))
        }
        btnGas.setOnClickListener {
            startActivity(Intent(this, GasActivity::class.java))
        }

        // 🔁 Schedule the Daily Report Worker (runs once a day)
/*        val workRequest = PeriodicWorkRequestBuilder<ReportWorker>(15, TimeUnit.MINUTES)
            .setInitialDelay(0, TimeUnit.MINUTES) // ⏱️ Delay for testing; change to 0 for real use
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_report_work",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )*/
        val testWorkRequest = OneTimeWorkRequestBuilder<ReportWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(this).enqueue(testWorkRequest)

    }
}
