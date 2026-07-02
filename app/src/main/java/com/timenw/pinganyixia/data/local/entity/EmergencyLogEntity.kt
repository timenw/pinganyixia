package com.timenw.pinganyixia.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergency_logs")
data class EmergencyLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val contactName: String,
    val phone: String,
    val actionType: String,
    val createdAt: Long = System.currentTimeMillis()
)