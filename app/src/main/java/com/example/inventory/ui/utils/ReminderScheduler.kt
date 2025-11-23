package com.example.inventory.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.inventory.Alarma.MyAlarmReceiver

object ReminderScheduler {

    /**
     * Programa un recordatorio usando AlarmManager.
     *
     * @param context Contexto de la app
     * @param noteId ID único de la nota (para que la notificación no choque con otras)
     * @param noteTitle Título que queremos mostrar en la notificación
     * @param timeMillis Momento exacto en tiempo real (System.currentTimeMillis())
     */
    fun scheduleReminder(
        context: Context,
        noteId: Int,
        noteTitle: String,
        timeMillis: Long
    ) {

        val intent = Intent(context, MyAlarmReceiver::class.java).apply {
            putExtra("noteId", noteId)
            putExtra("noteTitle", noteTitle)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            noteId,   // ← cada nota tendrá su propio recordatorio
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeMillis,
            pendingIntent
        )
    }

    /**
     * Cancela un recordatorio previamente programado.
     */
    fun cancelReminder(context: Context, noteId: Int) {

        val intent = Intent(context, MyAlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            noteId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}
