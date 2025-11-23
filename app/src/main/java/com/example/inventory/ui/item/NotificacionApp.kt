package com.example.inventory.ui.item

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.inventory.utils.NotificationUtils

class NotificacionApp : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationUtils.createNotificationChannel(this)
    }
}
