package com.example.habitstracker.utils

import com.example.habitstracker.data.entity.HabitLog
import java.time.LocalDate

object StreakUtils {

    fun calculateStreak(
        logs: List<HabitLog>
    ): Int {

        val dates = logs
            .filter { it.completed }
            .map { LocalDate.parse(it.date) }
            .toSet()

        var streak = 0

        var current = LocalDate.now()

        while (dates.contains(current)) {

            streak++

            current = current.minusDays(1)
        }

        return streak
    }
}

