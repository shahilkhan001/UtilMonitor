package com.example.utilmonitor

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.utilmonitor.data.UsageDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity() {

    private lateinit var tvHistory: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        tvHistory = findViewById(R.id.tvHistory)

        CoroutineScope(Dispatchers.IO).launch {
            val dao = UsageDatabase.getDatabase(this@HistoryActivity).usageDao()
            val data = dao.getAllUsage()
            withContext(Dispatchers.Main) {
                tvHistory.text = data.joinToString("\n\n") {
                    "${it.date}\n• Electricity: ${it.electricity} kWh\n• Gas: ${it.gas} m³\n• Water: ${it.water} L"
                }
            }
        }
    }
}