package com.example.habitstracker.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_logs",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = WorkoutType::class,
            parentColumns = ["typeId"],
            childColumns = ["typeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("userId"),
        Index("typeId")
    ]
)
data class WorkoutLog(

    @PrimaryKey(autoGenerate = true)
    val logId: Long = 0,

    val userId: Long,

    val typeId: Long,

    val workoutName: String,

    val date: String,

    val duration: Int,

    val reps: Int,

    val distance: Float,

    val calories: Int,

    val note: String?
)