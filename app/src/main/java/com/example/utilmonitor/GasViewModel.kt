package com.example.utilmonitor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class GasViewModel : ViewModel() {
    private val _gasUsage = MutableLiveData<Int>()
    val gasUsage: LiveData<Int> get() = _gasUsage

    init {
        updateGasUsage()
    }

    private fun updateGasUsage() {
        _gasUsage.value = Random.nextInt(50, 200) // Simulated gas usage in m³
    }

    fun refreshData() {
        updateGasUsage() // Simulate real-time updates
    }
}
