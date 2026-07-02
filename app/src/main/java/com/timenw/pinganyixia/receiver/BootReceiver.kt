package com.timenw.pinganyixia.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.timenw.pinganyixia.notification.NotificationHelper

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            NotificationHelper.createChannels(context)
            ReminderReceiver.rescheduleAllAlarms(context)
        }
    }
}