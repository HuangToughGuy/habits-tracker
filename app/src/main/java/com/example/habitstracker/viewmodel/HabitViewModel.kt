package com.example.habitstracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.data.entity.Habit
import com.example.habitstracker.data.entity.HabitLog
import com.example.habitstracker.data.repository.HabitRepository
import kotlinx.coroutines.launch

class HabitViewModel(
    private val repository: HabitRepository
) : ViewModel() {

    val allHabits = repository.getAllHabits()

    val habitsWithLogs =
        repository.getHabitWithLogs()

    val habitList =
        repository.getAllHabits()

    fun getHabitById(habitId: Long) =
        repository.getHabitById(habitId)

    fun insertHabit(habit: Habit) {
        viewModelScope.launch {
            repository.insertHabit(habit)
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
        }
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
            repository.updateHabit(habit)
        }
    }
    fun insertHabitLog(log: HabitLog) {

        viewModelScope.launch {

            repository.insertHabitLog(log)
        }
    }
    fun insertOrUpdateHabitLog(
        log: HabitLog
    ) {

        viewModelScope.launch {

            repository.insertOrUpdateHabitLog(log)
        }
    }
    suspend fun getHabitLogsList(
        habitId: Long
    ) =
        repository.getHabitLogsList(habitId)
}

