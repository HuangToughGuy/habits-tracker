package com.example.habitstracker.data.dao

import androidx.room.*
import com.example.habitstracker.data.entity.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Insert
    suspend fun insertReminder(reminder: Reminder): Long

    @Update
    suspend fun updateReminder(reminder: Reminder)

    @Delete
    suspend fun deleteReminder(reminder: Reminder)

    @Query("""
    SELECT * FROM reminders
    ORDER BY reminderId DESC
    """)
    fun getAllReminders(): Flow<List<Reminder>>

    @Query("""
        SELECT * FROM reminders
        WHERE habitId = :habitId
    """)
    fun getRemindersByHabit(habitId: Long): Flow<List<Reminder>>

    @Query("""
    SELECT * FROM reminders
    WHERE reminderId = :reminderId
""")
    fun getReminderById(reminderId: Long): Flow<Reminder?>
}