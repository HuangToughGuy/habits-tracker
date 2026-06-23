package com.example.habitstracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_types")
data class WorkoutType(

    @PrimaryKey(autoGenerate = true)
    val typeId: Long = 0,

    val name: String,

    val category: String
)