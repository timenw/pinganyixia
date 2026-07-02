package com.timenw.pinganyixia.data.repository

import android.content.Context
import com.timenw.pinganyixia.data.local.AppDatabase
import com.timenw.pinganyixia.data.local.entity.EmergencyLogEntity

class EmergencyLogRepository(context: Context) {
    private val dao = AppDatabase.getDatabase(context).emergencyLogDao()
    suspend fun getRecent(limit: Int = 20): List<EmergencyLogEntity> = dao.getRecent(limit)
    suspend fun logEmergency(contactName: String, phone: String, actionType: String): Long =
        dao.insert(EmergencyLogEntity(contactName = contactName, phone = phone, actionType = actionType))
}
