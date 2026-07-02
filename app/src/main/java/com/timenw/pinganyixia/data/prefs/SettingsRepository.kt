package com.timenw.pinganyixia.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class AppSettings(
    val fontScale: Float = 1.0f,
    val voiceEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val dailyCheckinReminderTime: String = "20:00",
    val missingCheckinAlertTime: String = "21:00",
    val emergencyMessageTemplate: String = "我现在需要帮助，请尽快联系我",
    val highContrastMode: Boolean = false,
    val simpleMode: Boolean = true
)

private val Context.settingsDataStore by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {
    private object Keys {
        val FONT_SCALE = floatPreferencesKey("font_scale")
        val VOICE_ENABLED = booleanPreferencesKey("voice_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val DAILY_CHECKIN_REMINDER = stringPreferencesKey("daily_checkin_reminder")
        val MISSING_CHECKIN_ALERT = stringPreferencesKey("missing_checkin_alert")
        val EMERGENCY_MESSAGE = stringPreferencesKey("emergency_message")
        val HIGH_CONTRAST = booleanPreferencesKey("high_contrast")
        val SIMPLE_MODE = booleanPreferencesKey("simple_mode")
    }

    fun settingsFlow(): Flow<AppSettings> = context.settingsDataStore.data.map { prefs ->
        AppSettings(
            fontScale = prefs[Keys.FONT_SCALE] ?: 1.0f,
            voiceEnabled = prefs[Keys.VOICE_ENABLED] ?: true,
            vibrationEnabled = prefs[Keys.VIBRATION_ENABLED] ?: true,
            dailyCheckinReminderTime = prefs[Keys.DAILY_CHECKIN_REMINDER] ?: "20:00",
            missingCheckinAlertTime = prefs[Keys.MISSING_CHECKIN_ALERT] ?: "21:00",
            emergencyMessageTemplate = prefs[Keys.EMERGENCY_MESSAGE] ?: "我现在需要帮助，请尽快联系我",
            highContrastMode = prefs[Keys.HIGH_CONTRAST] ?: false,
            simpleMode = prefs[Keys.SIMPLE_MODE] ?: true
        )
    }

    suspend fun setVoiceEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { it[Keys.VOICE_ENABLED] = enabled }
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { it[Keys.VIBRATION_ENABLED] = enabled }
    }

    suspend fun setHighContrastMode(enabled: Boolean) {
        context.settingsDataStore.edit { it[Keys.HIGH_CONTRAST] = enabled }
    }

    suspend fun setSimpleMode(enabled: Boolean) {
        context.settingsDataStore.edit { it[Keys.SIMPLE_MODE] = enabled }
    }
}
