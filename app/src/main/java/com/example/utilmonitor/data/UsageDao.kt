package com.example.utilmonitor.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: UsageEntry)

    @Query("SELECT * FROM usage_table ORDER BY date DESC")
    suspend fun getAllUsage(): List<UsageEntry>
}