package com.example.habitstracker.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.habitstracker.R

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        val enabled =
            intent.getBooleanExtra(
                "enabled",
                true
            )

        val repeatType =
            intent.getStringExtra("repeatType")
                ?: "Daily"

        val reminderId =
            intent.getLongExtra(
                "reminderId",
                -1
            )

        val habitName =
            intent.getStringExtra("habitName")
                ?: "Habit"

        val message =
            intent.getStringExtra("message")
                ?: ""

        val channelId = "habit_channel"

        val manager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Habit Reminder",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val notification =

            NotificationCompat.Builder(

                context,

                channelId

            )

                .setSmallIcon(R.drawable.ic_launcher_foreground)

                .setContentTitle(habitName)

                .setContentText(message)

                .setAutoCancel(true)

                .build()

        if (android.os.Build.VERSION.SDK_INT >=
            android.os.Build.VERSION_CODES.TIRAMISU
        ) {

            if (
                androidx.core.content.ContextCompat.checkSelfPermission(

                    context,

                    android.Manifest.permission.POST_NOTIFICATIONS

                ) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
        }
        manager.notify(

            System.currentTimeMillis().toInt(),

            notification
        )
        if (enabled) {

            val calendar = java.util.Calendar.getInstance()

            when (repeatType) {

                "Daily" ->

                    calendar.add(
                        java.util.Calendar.DAY_OF_YEAR,
                        1
                    )

                "Weekly" ->

                    calendar.add(
                        java.util.Calendar.DAY_OF_YEAR,
                        7
                    )

                "Monthly" ->

                    calendar.add(
                        java.util.Calendar.MONTH,
                        1
                    )
            }

            ReminderAlarmManager(context)
                .scheduleReminder(
                    reminderId = reminderId,
                    habitName = habitName,
                    message = message,
                    repeatType = repeatType,
                    enabled = true,
                    triggerTime = calendar.timeInMillis
                )
        }
    }
}



