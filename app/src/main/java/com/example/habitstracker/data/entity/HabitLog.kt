package com.example.habitstracker.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

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
    indices = [
        Index("habitId"),

        // Chỉ cho phép 1 HabitLog / Habit / Ngày
        Index(
            value = ["habitId", "date"],
            unique = true
        )
    ]
)
data class HabitLog(

    @PrimaryKey(autoGenerate = true)
    val logId: Long = 0,

    val habitId: Long,

    val date: String,

    val progress: Int = 0,

    val completed: Boolean,

    val completedTime: String? = null
)