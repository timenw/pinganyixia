package com.timenw.pinganyixia.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_checkins")
data class DailyCheckinEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val checkedInAt: Long,
    val note: String = ""
)