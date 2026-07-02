package com.timenw.pinganyixia.data.repository

import android.content.Context
import com.timenw.pinganyixia.data.local.AppDatabase
import com.timenw.pinganyixia.data.local.entity.MedicineEntity

class MedicineRepository(context: Context) {
    private val dao = AppDatabase.getDatabase(context).medicineDao()
    suspend fun getAllEnabled(): List<MedicineEntity> = dao.getAllEnabled()
    suspend fun insert(medicine: MedicineEntity): Long = dao.insert(medicine)
    suspend fun delete(id: Long) = dao.delete(id)
}
