package com.example.habitstracker.ui.model

import com.example.habitstracker.data.entity.Habit

data class HabitItem(

    val habit: Habit,

    val completedToday: Boolean,

    val streak: Int
)