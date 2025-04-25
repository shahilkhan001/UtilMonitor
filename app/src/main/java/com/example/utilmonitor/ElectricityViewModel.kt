package com.example.utilmonitor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class ElectricityViewModel : ViewModel() {
    private val _electricityUsage = MutableLiveData<Int>()
    val electricityUsage: LiveData<Int> get() = _electricityUsage

    init {
        updateElectricityUsage()
    }

    private fun updateElectricityUsage() {
        _electricityUsage.value = Random.nextInt(200, 500) // Simulated usage data
    }

    fun refreshData() {
        updateElectricityUsage()
    }
}
