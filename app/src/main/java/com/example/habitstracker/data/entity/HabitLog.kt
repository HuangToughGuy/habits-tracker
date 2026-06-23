package com.example.habitstracker.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "habit_logs",
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["habitId"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("habitId")]
)
data class HabitLog(

    @PrimaryKey(autoGenerate = true)
    val logId: Long = 0,

    val habitId: Long,

    val date: String,

    val progress: Int,

    val completed: Boolean,

    val completedTime: String?
)