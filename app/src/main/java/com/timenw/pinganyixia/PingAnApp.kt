package com.timenw.pinganyixia

import android.app.Application
import com.timenw.pinganyixia.notification.NotificationHelper

class PingAnApp : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createChannels(this)
    }
}
