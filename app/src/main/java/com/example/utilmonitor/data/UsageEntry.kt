package com.example.utilmonitor.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usage_table")
data class UsageEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val electricity: Int,
    val gas: Int,
    val water: Int
)
