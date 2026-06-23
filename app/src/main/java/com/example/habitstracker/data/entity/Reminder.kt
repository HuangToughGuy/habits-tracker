package com.example.habitstracker.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reminders",
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
data class Reminder(

    @PrimaryKey(autoGenerate = true)
    val reminderId: Long = 0,

    val habitId: Long,

    val time: String,

    val repeatType: String,

    val message: String,

    val enabled: Boolean
)