package com.example.habitstracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.data.entity.Reminder
import com.example.habitstracker.data.repository.ReminderRepository
import kotlinx.coroutines.launch

class ReminderViewModel(
    private val repository: ReminderRepository
) : ViewModel() {

    val reminderList =
        repository.getAllReminders()

    fun getRemindersByHabit(habitId: Long) =
        repository.getRemindersByHabit(habitId)

    suspend fun insertReminder(
        reminder: Reminder
    ): Long {

        return repository.insertReminder(
            reminder
        )
    }

    fun updateReminder(reminder: Reminder) {

        viewModelScope.launch {

            repository.updateReminder(reminder)
        }
    }

    fun deleteReminder(reminder: Reminder) {

        viewModelScope.launch {

            repository.deleteReminder(reminder)
        }
    }

    fun getReminderById(reminderId: Long) =
        repository.getReminderById(reminderId)
}