package com.example.habitstracker.data.repository

import com.example.habitstracker.data.dao.ReminderDao
import com.example.habitstracker.data.entity.Reminder

class ReminderRepository(
    private val reminderDao: ReminderDao
) {

    fun getEnabledReminders() =
        reminderDao.getEnabledReminders()

    fun getRemindersByHabit(habitId: Long) =
        reminderDao.getRemindersByHabit(habitId)

    suspend fun insertReminder(reminder: Reminder) =
        reminderDao.insertReminder(reminder)

    suspend fun updateReminder(reminder: Reminder) =
        reminderDao.updateReminder(reminder)

    suspend fun deleteReminder(reminder: Reminder) =
        reminderDao.deleteReminder(reminder)
}