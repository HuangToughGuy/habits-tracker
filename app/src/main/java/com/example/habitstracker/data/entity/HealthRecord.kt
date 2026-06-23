package com.example.habitstracker.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "health_records",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class HealthRecord(

    @PrimaryKey(autoGenerate = true)
    val recordId: Long = 0,

    val userId: Long,

    val date: String,

    val weight: Float,

    val waterIntake: Float,

    val sleepHours: Float,

    val heartRate: Int
)