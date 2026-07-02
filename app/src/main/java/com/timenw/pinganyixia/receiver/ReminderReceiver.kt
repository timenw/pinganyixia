package com.timenw.pinganyixia.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.timenw.pinganyixia.notification.NotificationHelper

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        when (action) {
            "com.timenw.pinganyixia.ACTION_DAILY_CHECKIN_REMINDER" -> {
                NotificationHelper.showReminderNotification(
                    context,
                    "每日报平安提醒",
                    "今天还没报平安哦，点击上报您的平安状态",
                    1001
                )
            }
            "com.timenw.pinganyixia.ACTION_MEDICINE_REMINDER" -> {
                val medicineName = intent.getStringExtra("medicine_name") ?: "药品"
                val time = intent.getStringExtra("time") ?: ""
                NotificationHelper.showReminderNotification(
                    context,
                    "用药提醒",
                    "该吃 $medicineName 了 ($time)",
                    intent.getIntExtra("notification_id", 2000)
                )
            }
            "com.timenw.pinganyixia.ACTION_MISSING_CHECKIN_ALERT" -> {
                NotificationHelper.showEmergencyNotification(
                    context,
                    "⚠️ 未报平安警报",
                    "今日截至目前未报平安，请确认长辈情况",
                    3001
                )
            }
        }
    }

    companion object {
        private const val DAILY_CHECKIN_ACTION = "com.timenw.pinganyixia.ACTION_DAILY_CHECKIN_REMINDER"
        private const val MEDICINE_ACTION = "com.timenw.pinganyixia.ACTION_MEDICINE_REMINDER"
        private const val MISSING_CHECKIN_ACTION = "com.timenw.pinganyixia.ACTION_MISSING_CHECKIN_ALERT"

        fun scheduleDailyCheckinReminder(context: Context, hour: Int, minute: Int) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, ReminderReceiver::class.java).apply {
                action = DAILY_CHECKIN_ACTION
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context, 1001, intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val calendar = java.util.Calendar.getInstance().apply {
                set(java.util.Calendar.HOUR_OF_DAY, hour)
                set(java.util.Calendar.MINUTE, minute)
                set(java.util.Calendar.SECOND, 0)
                if (timeInMillis < System.currentTimeMillis()) {
                    add(java.util.Calendar.DAY_OF_MONTH, 1)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
        }

        fun scheduleMedicineReminder(
            context: Context,
            medicineId: Long,
            medicineName: String,
            timeText: String,
            hour: Int,
            minute: Int
        ) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val notificationId = 2000 + medicineId.toInt()

            val intent = Intent(context, ReminderReceiver::class.java).apply {
                action = MEDICINE_ACTION
                putExtra("medicine_name", medicineName)
                putExtra("time", timeText)
                putExtra("notification_id", notificationId)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context, notificationId, intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val calendar = java.util.Calendar.getInstance().apply {
                set(java.util.Calendar.HOUR_OF_DAY, hour)
                set(java.util.Calendar.MINUTE, minute)
                set(java.util.Calendar.SECOND, 0)
                if (timeInMillis < System.currentTimeMillis()) {
                    add(java.util.Calendar.DAY_OF_MONTH, 1)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
        }

        fun scheduleMissingCheckinAlert(context: Context, hour: Int, minute: Int) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, ReminderReceiver::class.java).apply {
                action = MISSING_CHECKIN_ACTION
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context, 3001, intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val calendar = java.util.Calendar.getInstance().apply {
                set(java.util.Calendar.HOUR_OF_DAY, hour)
                set(java.util.Calendar.MINUTE, minute)
                set(java.util.Calendar.SECOND, 0)
                if (timeInMillis < System.currentTimeMillis()) {
                    add(java.util.Calendar.DAY_OF_MONTH, 1)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
        }

        fun rescheduleAllAlarms(context: Context) {
            // Will be called from MainViewModel or repository to reschedule
            NotificationHelper.createChannels(context)
        }
    }
}