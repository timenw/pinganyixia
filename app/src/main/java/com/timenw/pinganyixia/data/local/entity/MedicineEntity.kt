package com.timenw.pinganyixia.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class MedicineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val dosage: String,
    val note: String = "",
    val enabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)