package com.timenw.pinganyixia.data.repository

import android.content.Context
import com.timenw.pinganyixia.data.local.AppDatabase
import com.timenw.pinganyixia.data.local.entity.ContactEntity

class ContactRepository(context: Context) {
    private val dao = AppDatabase.getDatabase(context).contactDao()
    suspend fun getAll(): List<ContactEntity> = dao.getAll()
    suspend fun getFirstEmergency(): ContactEntity? = dao.getFirstEmergency()
    suspend fun insert(contact: ContactEntity): Long = dao.insert(contact)
    suspend fun delete(id: Long) = dao.delete(id)
}
