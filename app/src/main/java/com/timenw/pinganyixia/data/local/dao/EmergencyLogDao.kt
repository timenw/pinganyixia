package com.timenw.pinganyixia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.timenw.pinganyixia.data.local.entity.EmergencyLogEntity

@Dao
interface EmergencyLogDao {
    @Query("SELECT * FROM emergency_logs ORDER BY createdAt DESC LIMIT :limit")
    suspend fun getRecent(limit: Int): List<EmergencyLogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: EmergencyLogEntity): Long
}
