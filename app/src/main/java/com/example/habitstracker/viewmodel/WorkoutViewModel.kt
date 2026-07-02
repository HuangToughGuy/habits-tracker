package com.example.habitstracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.data.entity.WorkoutLog
import com.example.habitstracker.data.entity.WorkoutType
import com.example.habitstracker.data.repository.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    val workoutTypes = repository.getAllWorkoutTypes()

    val workoutLogs = repository.getAllWorkoutLogs()

    val latestWorkout = repository.getLatestWorkout()

    val latestWorkoutSummary = repository.getLatestWorkoutSummary()

    fun getWorkoutLogById(
        logId: Long
    ) = repository.getWorkoutLogById(logId)

    fun insertWorkoutType(
        type: WorkoutType
    ) {

        viewModelScope.launch {

            repository.insertWorkoutType(type)
        }
    }

    suspend fun insertWorkoutLog(
        log: WorkoutLog
    ): Long {

        return repository.insertWorkoutLog(log)
    }

    fun updateWorkoutLog(
        log: WorkoutLog
    ) {

        viewModelScope.launch {

            repository.updateWorkoutLog(log)
        }
    }

    fun deleteWorkoutLog(
        log: WorkoutLog
    ) {

        viewModelScope.launch {

            repository.deleteWorkoutLog(log)
        }
    }


}