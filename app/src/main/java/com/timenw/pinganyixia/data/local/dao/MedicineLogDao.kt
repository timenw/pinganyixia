package com.timenw.pinganyixia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.timenw.pinganyixia.data.local.entity.MedicineLogEntity

@Dao
interface MedicineLogDao {
    @Query("SELECT * FROM medicine_logs WHERE date = :date ORDER BY takenAt DESC")
    suspend fun getByDate(date: String): List<MedicineLogEntity>

    @Query("SELECT * FROM medicine_logs WHERE medicineId = :medicineId AND date = :date AND scheduledTime = :time LIMIT 1")
    suspend fun getByMedicineAndTime(medicineId: Long, date: String, time: String): MedicineLogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: MedicineLogEntity): Long
}
