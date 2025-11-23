package com.example.inventory.Alarma

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.inventory.R
import com.example.inventory.utils.NotificationUtils

class MyAlarmReceiver : BroadcastReceiver() {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent?) {

        val noteTitle = intent?.getStringExtra("title") ?: "Recordatorio"
        val noteMessage = intent?.getStringExtra("message") ?: "Tienes tareas pendientes."

        // Crear el canal si no existe
        NotificationUtils.createNotificationChannel(context)

        // Construir la notificación usando el canal correcto
        val notification = NotificationCompat.Builder(context, NotificationUtils.CHANNEL_ID)
            .setSmallIcon(R.drawable.notifications_24)  // ✔ ICONO CORRECTO
            .setContentTitle(noteTitle)
            .setContentText(noteMessage)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(1001, notification)
    }
}
