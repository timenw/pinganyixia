package com.timenw.pinganyixia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.timenw.pinganyixia.data.local.entity.DailyCheckinEntity

@Dao
interface DailyCheckinDao {
    @Query("SELECT * FROM daily_checkins WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: String): DailyCheckinEntity?

    @Query("SELECT * FROM daily_checkins ORDER BY date DESC LIMIT :limit")
    suspend fun getRecent(limit: Int): List<DailyCheckinEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(checkin: DailyCheckinEntity): Long
}
