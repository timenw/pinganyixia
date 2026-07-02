package com.timenw.pinganyixia.data.repository

import android.content.Context
import com.timenw.pinganyixia.data.local.AppDatabase
import com.timenw.pinganyixia.data.local.entity.MedicineLogEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MedicineLogRepository(context: Context) {
    private val dao = AppDatabase.getDatabase(context).medicineLogDao()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    suspend fun getTodayLogs(): List<MedicineLogEntity> = dao.getByDate(LocalDate.now().format(formatter))
    suspend fun logTaken(medicineId: Long, medicineName: String, scheduledTime: String = "现在"): Long {
        val today = LocalDate.now().format(formatter)
        val existing = dao.getByMedicineAndTime(medicineId, today, scheduledTime)
        if (existing != null) return existing.id
        return dao.insert(MedicineLogEntity(medicineId = medicineId, medicineName = medicineName, scheduledTime = scheduledTime, takenAt = System.currentTimeMillis(), date = today))
    }
}
