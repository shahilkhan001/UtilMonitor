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

class GasActivity : AppCompatActivity() {
    private lateinit var gasChartView: LineChartView
    private val gasDataList = mutableListOf<Int>()

    private val viewModel: GasViewModel by viewModels()

    private lateinit var etGasLimit: EditText
    private lateinit var btnSaveGasLimit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gas)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.BLACK

        val tvGasData = findViewById<TextView>(R.id.tvGasData)
        val btnRefresh = findViewById<Button>(R.id.btnRefresh)
        etGasLimit = findViewById(R.id.etGasLimit)
        btnSaveGasLimit = findViewById(R.id.btnSaveGasLimit)
        gasChartView = findViewById(R.id.gasCustomChart)

        // Save Gas limit button
        btnSaveGasLimit.setOnClickListener {
            val limit = etGasLimit.text.toString().toIntOrNull()
            if (limit != null) {
                PreferenceHelper.setLimit(this, "gas_limit", limit)
                Toast.makeText(this, "Gas limit saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Enter a valid number", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe and show gas usage data
        viewModel.gasUsage.observe(this) { usage ->
            tvGasData.text = "Current Gas Usage: $usage m³"
            gasDataList.add(usage)
            gasChartView.setData(gasDataList)

            val limit = PreferenceHelper.getLimit(this, "gas_limit")
            if (limit != -1 && usage > limit) {
                checkNotificationPermissionAndNotify("Gas Alert", "Gas usage exceeded $limit m³")
            }
        }

        btnRefresh.setOnClickListener {
            viewModel.refreshData()
        }
    }

    // Function to check and request permission if needed, then show notification
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

    // Permission request launcher for Android 13 and above
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to show notification
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
