package com.timenw.pinganyixia.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.timenw.pinganyixia.data.local.dao.ContactDao
import com.timenw.pinganyixia.data.local.dao.DailyCheckinDao
import com.timenw.pinganyixia.data.local.dao.EmergencyLogDao
import com.timenw.pinganyixia.data.local.dao.MedicineDao
import com.timenw.pinganyixia.data.local.dao.MedicineLogDao
import com.timenw.pinganyixia.data.local.dao.MedicineReminderDao
import com.timenw.pinganyixia.data.local.entity.ContactEntity
import com.timenw.pinganyixia.data.local.entity.DailyCheckinEntity
import com.timenw.pinganyixia.data.local.entity.EmergencyLogEntity
import com.timenw.pinganyixia.data.local.entity.MedicineEntity
import com.timenw.pinganyixia.data.local.entity.MedicineLogEntity
import com.timenw.pinganyixia.data.local.entity.MedicineReminderEntity

@Database(
    entities = [
        ContactEntity::class,
        MedicineEntity::class,
        MedicineReminderEntity::class,
        DailyCheckinEntity::class,
        MedicineLogEntity::class,
        EmergencyLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
    abstract fun medicineDao(): MedicineDao
    abstract fun medicineReminderDao(): MedicineReminderDao
    abstract fun dailyCheckinDao(): DailyCheckinDao
    abstract fun medicineLogDao(): MedicineLogDao
    abstract fun emergencyLogDao(): EmergencyLogDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pingan_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}