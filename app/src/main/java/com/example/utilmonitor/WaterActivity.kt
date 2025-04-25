package com.example.utilmonitor

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class WaterActivity : AppCompatActivity() {

    private lateinit var waterChartView: LineChartView
    private val waterDataList = mutableListOf<Int>()

    private lateinit var etWaterLimit: EditText
    private lateinit var btnSaveWaterLimit: Button

    private val viewModel: WaterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_water)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.BLACK

        val tvWaterData = findViewById<TextView>(R.id.tvWaterData)
        val btnRefresh = findViewById<Button>(R.id.btnRefresh)
        etWaterLimit = findViewById(R.id.etWaterLimit)
        btnSaveWaterLimit = findViewById(R.id.btnSaveWaterLimit)
        waterChartView = findViewById(R.id.waterCustomChart)

        // Save Limit button
        btnSaveWaterLimit.setOnClickListener {
            val limit = etWaterLimit.text.toString().toIntOrNull()
            if (limit != null) {
                PreferenceHelper.setLimit(this, "water_limit", limit)
                Toast.makeText(this, "Limit saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Enter a valid number", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe and show water usage data
        viewModel.waterUsage.observe(this) { usage ->
            tvWaterData.text = "Current Water Usage: $usage L"
            waterDataList.add(usage)
            waterChartView.setData(waterDataList)

            val limit = PreferenceHelper.getLimit(this, "water_limit")
            if (limit != -1 && usage > limit) {
                checkNotificationPermissionAndNotify("Water Usage Alert", "Usage exceeded $limit L")
            }
        }

        btnRefresh.setOnClickListener {
            viewModel.refreshData()
        }
    }

    private fun checkNotificationPermissionAndNotify(title: String, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                    showNotification(title, message)
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            showNotification(title, message)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "usage_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Usage Alerts", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
    }
}
