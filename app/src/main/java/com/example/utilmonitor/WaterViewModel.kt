package com.example.utilmonitor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class WaterViewModel : ViewModel() {
    private val _waterUsage = MutableLiveData<Int>()
    val waterUsage: LiveData<Int> get() = _waterUsage

    init {
        updateWaterUsage()
    }

    private fun updateWaterUsage() {
        _waterUsage.value = Random.nextInt(100, 1000)
    }

    fun refreshData() {
        updateWaterUsage()
    }
}
