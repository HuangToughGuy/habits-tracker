package com.example.habitstracker.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.habitstracker.data.entity.Habit
import com.example.habitstracker.data.entity.HabitLog

data class HabitWithLogs(

    @Embedded
    val habit: Habit,

    @Relation(
        parentColumn = "habitId",
        entityColumn = "habitId"
    )
    val logs: List<HabitLog>
)