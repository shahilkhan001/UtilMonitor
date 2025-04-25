package com.example.utilmonitor

import android.content.Context
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.utilmonitor.data.UsageDatabase
import com.example.utilmonitor.data.UsageEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ReportWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // Generate random values for electricity, gas, and water usage
        val electricity = (100..500).random()
        val gas = (10..50).random()
        val water = (50..200).random()

        // Get the current date
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Create a new UsageEntry object
        val entry = UsageEntry(date = date, electricity = electricity, gas = gas, water = water)

        // Insert the entry into the database on a background thread
        val dao = UsageDatabase.getDatabase(applicationContext).usageDao()

        CoroutineScope(Dispatchers.IO).launch {
            dao.insert(entry)
        }

        // Show the daily usage summary notification
        showReportNotification("UtilMonitor Daily Report", """
            Daily Usage:
            • Electricity: $electricity kWh
            • Gas: $gas m³
            • Water: $water L
        """.trimIndent())

        return Result.success()
    }

    private fun showReportNotification(title: String, message: String) {
        val channelId = "report_channel"
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Reports", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText("Tap to view summary")
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .build()

        manager.notify(2, notification)
    }
}
