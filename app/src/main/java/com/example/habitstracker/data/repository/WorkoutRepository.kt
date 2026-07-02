package com.example.habitstracker.data.repository

import com.example.habitstracker.data.dao.WorkoutDao
import com.example.habitstracker.data.entity.WorkoutLog
import com.example.habitstracker.data.entity.WorkoutType

class WorkoutRepository(
    private val workoutDao: WorkoutDao
) {

    // Workout Type

    fun getAllWorkoutTypes() = workoutDao.getAllWorkoutTypes()

    suspend fun insertWorkoutType(
        type: WorkoutType
    ) = workoutDao.insertWorkoutType(type)

    // Workout Log

    fun getAllWorkoutLogs() = workoutDao.getAllWorkoutLogs()

    fun getWorkoutLogById(
        logId: Long
    ) = workoutDao.getWorkoutLogById(logId)

    suspend fun insertWorkoutLog(
        log: WorkoutLog
    ) = workoutDao.insertWorkoutLog(log)

    suspend fun updateWorkoutLog(
        log: WorkoutLog
    ) = workoutDao.updateWorkoutLog(log)

    suspend fun deleteWorkoutLog(
        log: WorkoutLog
    ) = workoutDao.deleteWorkoutLog(log)
}