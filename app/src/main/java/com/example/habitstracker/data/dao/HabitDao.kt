package com.example.habitstracker.data.dao

import androidx.room.*
import com.example.habitstracker.data.entity.Habit
import com.example.habitstracker.data.entity.HabitLog
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Insert
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM habits ORDER BY createdDate DESC")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE habitId = :habitId")
    fun getHabitById(habitId: Long): Flow<Habit?>

    // HabitLog

    @Insert
    suspend fun insertHabitLog(log: HabitLog): Long

    @Update
    suspend fun updateHabitLog(log: HabitLog)

    @Delete
    suspend fun deleteHabitLog(log: HabitLog)

    @Query("""
        SELECT * FROM habit_logs
        WHERE habitId = :habitId
        ORDER BY date DESC
    """)
    fun getHabitLogs(habitId: Long): Flow<List<HabitLog>>
}