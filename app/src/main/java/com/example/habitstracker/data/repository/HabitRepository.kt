package com.example.habitstracker.data.repository

import com.example.habitstracker.data.dao.HabitDao
import com.example.habitstracker.data.entity.Habit
import com.example.habitstracker.data.entity.HabitLog

class HabitRepository(
    private val habitDao: HabitDao
) {

    fun getAllHabits() =
        habitDao.getAllHabits()

    fun getHabitById(habitId: Long) =
        habitDao.getHabitById(habitId)

    suspend fun insertHabit(habit: Habit) =
        habitDao.insertHabit(habit)

    suspend fun updateHabit(habit: Habit) =
        habitDao.updateHabit(habit)

    suspend fun deleteHabit(habit: Habit) =
        habitDao.deleteHabit(habit)

    fun getHabitLogs(habitId: Long) =
        habitDao.getHabitLogs(habitId)

    suspend fun insertHabitLog(log: HabitLog) =
        habitDao.insertHabitLog(log)
}