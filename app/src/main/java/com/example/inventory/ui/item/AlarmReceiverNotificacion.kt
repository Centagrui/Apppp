package com.example.inventory.ui.item

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.inventory.R
import com.example.inventory.utils.NotificationUtils

class AlarmReceiverNotificacion : BroadcastReceiver() {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        val title = intent?.getStringExtra("title") ?: "Recordatorio"
        val message = intent?.getStringExtra("message") ?: "No olvides tu tarea."

        // Crear canal de notificación si no existe
        NotificationUtils.createNotificationChannel(context)

        val notification = NotificationCompat.Builder(context, NotificationUtils.CHANNEL_ID)
            .setSmallIcon(R.drawable.notifications_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(1001, notification)
    }
}
