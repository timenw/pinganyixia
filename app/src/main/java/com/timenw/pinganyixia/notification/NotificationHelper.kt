package com.timenw.pinganyixia.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.timenw.pinganyixia.MainActivity

object NotificationHelper {
    private const val CHANNEL_REMINDER = "reminder_channel"
    private const val CHANNEL_EMERGENCY = "emergency_channel"

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val reminderChannel = NotificationChannel(
                CHANNEL_REMINDER,
                "日常提醒",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "用药提醒、报平安提醒等日常通知"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 200, 100, 200)
            }

            val emergencyChannel = NotificationChannel(
                CHANNEL_EMERGENCY,
                "紧急警报",
                NotificationManager.IMPORTANCE_MAX
            ).apply {
                description = "未报平安警报、紧急求助等高优先级通知"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
                setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), null)
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannels(listOf(reminderChannel, emergencyChannel))
        }
    }

    fun showReminderNotification(context: Context, title: String, content: String, notificationId: Int) {
        if (!canPostNotifications(context)) return
        val pendingIntent = contentIntent(context, notificationId)
        val notification = NotificationCompat.Builder(context, CHANNEL_REMINDER)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    fun showEmergencyNotification(context: Context, title: String, content: String, notificationId: Int) {
        if (!canPostNotifications(context)) return
        val pendingIntent = contentIntent(context, notificationId)
        val notification = NotificationCompat.Builder(context, CHANNEL_EMERGENCY)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .setOngoing(true)
            .setFullScreenIntent(pendingIntent, true)
            .build()
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    private fun contentIntent(context: Context, requestCode: Int): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun canPostNotifications(context: Context): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }
}
