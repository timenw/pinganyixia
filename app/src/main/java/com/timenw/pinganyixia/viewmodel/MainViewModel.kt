package com.timenw.pinganyixia.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.timenw.pinganyixia.data.local.entity.ContactEntity
import com.timenw.pinganyixia.data.local.entity.DailyCheckinEntity
import com.timenw.pinganyixia.data.local.entity.EmergencyLogEntity
import com.timenw.pinganyixia.data.local.entity.MedicineEntity
import com.timenw.pinganyixia.data.local.entity.MedicineLogEntity
import com.timenw.pinganyixia.data.prefs.AppSettings
import com.timenw.pinganyixia.data.prefs.SettingsRepository
import com.timenw.pinganyixia.data.repository.CheckinRepository
import com.timenw.pinganyixia.data.repository.ContactRepository
import com.timenw.pinganyixia.data.repository.EmergencyLogRepository
import com.timenw.pinganyixia.data.repository.MedicineLogRepository
import com.timenw.pinganyixia.data.repository.MedicineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(context: Context) : ViewModel() {
    private val appContext = context.applicationContext
    private val settingsRepository = SettingsRepository(appContext)
    private val contactRepository = ContactRepository(appContext)
    private val medicineRepository = MedicineRepository(appContext)
    private val medicineLogRepository = MedicineLogRepository(appContext)
    private val checkinRepository = CheckinRepository(appContext)
    private val emergencyLogRepository = EmergencyLogRepository(appContext)

    val settings = settingsRepository.settingsFlow()

    private val _contacts = MutableStateFlow<List<ContactEntity>>(emptyList())
    val contacts: StateFlow<List<ContactEntity>> = _contacts.asStateFlow()

    private val _medicines = MutableStateFlow<List<MedicineEntity>>(emptyList())
    val medicines: StateFlow<List<MedicineEntity>> = _medicines.asStateFlow()

    private val _todayCheckin = MutableStateFlow<DailyCheckinEntity?>(null)
    val todayCheckin: StateFlow<DailyCheckinEntity?> = _todayCheckin.asStateFlow()

    private val _todayLogs = MutableStateFlow<List<MedicineLogEntity>>(emptyList())
    val todayLogs: StateFlow<List<MedicineLogEntity>> = _todayLogs.asStateFlow()

    private val _recentEmergencies = MutableStateFlow<List<EmergencyLogEntity>>(emptyList())
    val recentEmergencies: StateFlow<List<EmergencyLogEntity>> = _recentEmergencies.asStateFlow()

    private val _lastMessage = MutableStateFlow<String?>(null)
    val lastMessage: StateFlow<String?> = _lastMessage.asStateFlow()

    init {
        refreshAll()
    }

    fun refreshAll() = viewModelScope.launch(Dispatchers.IO) {
        _contacts.value = contactRepository.getAll()
        _medicines.value = medicineRepository.getAllEnabled()
        _todayCheckin.value = checkinRepository.getTodayCheckin()
        _todayLogs.value = medicineLogRepository.getTodayLogs()
        _recentEmergencies.value = emergencyLogRepository.getRecent(10)
    }

    fun checkin() = viewModelScope.launch(Dispatchers.IO) {
        checkinRepository.checkin()
        _todayCheckin.value = checkinRepository.getTodayCheckin()
        _lastMessage.value = "已记录今天平安"
    }

    fun addContact(name: String, relation: String, phone: String, isEmergency: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        if (name.isBlank() || phone.isBlank()) {
            _lastMessage.value = "联系人姓名和手机号不能为空"
            return@launch
        }
        contactRepository.insert(
            ContactEntity(
                name = name.trim(),
                relation = relation.ifBlank { "家人" }.trim(),
                phone = phone.trim(),
                isEmergency = isEmergency
            )
        )
        _contacts.value = contactRepository.getAll()
        _lastMessage.value = "已添加联系人"
    }

    fun deleteContact(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        contactRepository.delete(id)
        _contacts.value = contactRepository.getAll()
        _lastMessage.value = "已删除联系人"
    }

    fun addMedicine(name: String, dosage: String) = viewModelScope.launch(Dispatchers.IO) {
        if (name.isBlank()) {
            _lastMessage.value = "药品名称不能为空"
            return@launch
        }
        medicineRepository.insert(
            MedicineEntity(
                name = name.trim(),
                dosage = dosage.ifBlank { "按医嘱" }.trim()
            )
        )
        _medicines.value = medicineRepository.getAllEnabled()
        _lastMessage.value = "已添加用药提醒"
    }

    fun deleteMedicine(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        medicineRepository.delete(id)
        _medicines.value = medicineRepository.getAllEnabled()
        _lastMessage.value = "已删除药品"
    }

    fun markMedicineTaken(medicine: MedicineEntity) = viewModelScope.launch(Dispatchers.IO) {
        medicineLogRepository.logTaken(medicine.id, medicine.name)
        _todayLogs.value = medicineLogRepository.getTodayLogs()
        _lastMessage.value = "已记录服药：${medicine.name}"
    }

    fun triggerEmergency() = viewModelScope.launch(Dispatchers.IO) {
        val emergencyContact = contactRepository.getFirstEmergency() ?: _contacts.value.firstOrNull()
        if (emergencyContact == null) {
            _lastMessage.value = "请先添加紧急联系人"
            return@launch
        }
        emergencyLogRepository.logEmergency(emergencyContact.name, emergencyContact.phone, "CALL")
        _recentEmergencies.value = emergencyLogRepository.getRecent(10)
        _lastMessage.value = "已记录求助：${emergencyContact.name}"
    }

    fun clearMessage() {
        _lastMessage.value = null
    }

    fun updateVoiceEnabled(enabled: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        settingsRepository.setVoiceEnabled(enabled)
    }

    fun updateDailyCheckinReminderTime(time: String) = viewModelScope.launch(Dispatchers.IO) {
        settingsRepository.setDailyCheckinReminderTime(time)
        // Reschedule alarms
        val context = appContext
        val settings = settingsRepository.settingsFlow().first()
        val (hour, minute) = time.split(':").map { it.toInt() }
        ReminderReceiver.scheduleDailyCheckinReminder(context, hour, minute)
        ReminderReceiver.scheduleMissingCheckinAlert(context, settings.missingCheckinAlertTime)
    }

    fun updateMissingCheckinAlertTime(time: String) = viewModelScope.launch(Dispatchers.IO) {
        settingsRepository.setMissingCheckinAlertTime(time)
        // Reschedule alarms
        val context = appContext
        val settings = settingsRepository.settingsFlow().first()
        val (hour, minute) = time.split(':").map { it.toInt() }
        ReminderReceiver.scheduleMissingCheckinAlert(context, hour, minute)
    }

    fun updateEmergencyMessageTemplate(message: String) = viewModelScope.launch(Dispatchers.IO) {
        settingsRepository.setEmergencyMessageTemplate(message)
    }


    fun updateVibrationEnabled(enabled: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        settingsRepository.setVibrationEnabled(enabled)
    }

    fun updateHighContrastMode(enabled: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        settingsRepository.setHighContrastMode(enabled)
    }

    fun updateSimpleMode(enabled: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        settingsRepository.setSimpleMode(enabled)
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(context) as T
            }
        }
    }
}
