package com.timenw.pinganyixia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.timenw.pinganyixia.data.local.entity.MedicineReminderEntity

@Dao
interface MedicineReminderDao {
    @Query("SELECT * FROM medicine_reminders WHERE medicineId = :medicineId AND enabled = 1 ORDER BY timeText ASC")
    suspend fun getByMedicineId(medicineId: Long): List<MedicineReminderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminder: MedicineReminderEntity): Long

    @Query("DELETE FROM medicine_reminders WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM medicine_reminders WHERE medicineId = :medicineId")
    suspend fun deleteByMedicineId(medicineId: Long)
}
