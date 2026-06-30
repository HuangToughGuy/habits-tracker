package com.example.habitstracker.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class ReminderAlarmManager(
    private val context: Context
) {

    fun scheduleReminder(
        reminderId: Long,
        habitName: String,
        message: String,
        triggerTime: Long
    ) {

        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager

        val intent = Intent(
            context,
            ReminderReceiver::class.java
        ).apply {

            putExtra("habitName", habitName)

            putExtra("message", message)
        }

        val pendingIntent =
            PendingIntent.getBroadcast(

                context,

                reminderId.toInt(),

                intent,

                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        alarmManager.setExactAndAllowWhileIdle(

            AlarmManager.RTC_WAKEUP,

            triggerTime,

            pendingIntent
        )
    }
    fun cancelReminder(reminderId: Long) {

        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager

        val intent = Intent(
            context,
            ReminderReceiver::class.java
        )

        val pendingIntent =
            PendingIntent.getBroadcast(

                context,

                reminderId.toInt(),

                intent,

                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        alarmManager.cancel(pendingIntent)
    }
}