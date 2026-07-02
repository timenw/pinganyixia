package com.timenw.pinganyixia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.timenw.pinganyixia.data.local.entity.ContactEntity

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts ORDER BY sortOrder ASC, createdAt DESC")
    suspend fun getAll(): List<ContactEntity>

    @Query("SELECT * FROM contacts WHERE isEmergency = 1 ORDER BY sortOrder ASC LIMIT 1")
    suspend fun getFirstEmergency(): ContactEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: ContactEntity): Long

    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun delete(id: Long)
}
