package com.example.habitstracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.example.habitstracker.data.dao.*

import com.example.habitstracker.data.entity.*

@Database(
    entities = [
        User::class,
        Habit::class,
        HabitLog::class,
        HealthRecord::class,
        WorkoutType::class,
        WorkoutLog::class,
        Reminder::class
    ],
    version = 4,
    exportSchema = false
)
abstract class HabitsTrackerDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun habitDao(): HabitDao
    abstract fun healthRecordDao(): HealthRecordDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun reminderDao(): ReminderDao
    companion object {

        @Volatile
        private var INSTANCE: HabitsTrackerDatabase? = null

        fun getInstance(context: Context): HabitsTrackerDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HabitsTrackerDatabase::class.java,
                    "habitstracker_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}