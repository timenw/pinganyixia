package com.timenw.pinganyixia.data.repository

import android.content.Context
import com.timenw.pinganyixia.data.local.AppDatabase
import com.timenw.pinganyixia.data.local.entity.DailyCheckinEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CheckinRepository(context: Context) {
    private val dao = AppDatabase.getDatabase(context).dailyCheckinDao()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    suspend fun getTodayCheckin(): DailyCheckinEntity? = dao.getByDate(LocalDate.now().format(formatter))
    suspend fun getRecent(limit: Int): List<DailyCheckinEntity> = dao.getRecent(limit)
    suspend fun checkin(): Long {
        val today = LocalDate.now().format(formatter)
        val existing = dao.getByDate(today)
        if (existing != null) return existing.id
        return dao.insert(DailyCheckinEntity(date = today, checkedInAt = System.currentTimeMillis()))
    }
}
