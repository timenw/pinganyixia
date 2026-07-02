package com.timenw.pinganyixia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.timenw.pinganyixia.data.local.entity.MedicineEntity

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicines WHERE enabled = 1 ORDER BY createdAt DESC")
    suspend fun getAllEnabled(): List<MedicineEntity>

    @Query("SELECT * FROM medicines ORDER BY createdAt DESC")
    suspend fun getAll(): List<MedicineEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicine: MedicineEntity): Long

    @Query("DELETE FROM medicines WHERE id = :id")
    suspend fun delete(id: Long)
}
