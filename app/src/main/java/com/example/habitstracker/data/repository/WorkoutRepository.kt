package com.example.habitstracker.data.repository

import com.example.habitstracker.data.dao.WorkoutDao
import com.example.habitstracker.data.entity.WorkoutLog
import com.example.habitstracker.data.entity.WorkoutType

class WorkoutRepository(
    private val workoutDao: WorkoutDao
) {

    fun getAllWorkoutTypes() = workoutDao.getAllWorkoutTypes()

    suspend fun insertWorkoutType(type: WorkoutType) =
        workoutDao.insertWorkoutType(type)

    fun getAllWorkoutLogs() = workoutDao.getAllWorkoutLogs()

    suspend fun insertWorkoutLog(log: WorkoutLog) = workoutDao.insertWorkoutLog(log)

    suspend fun updateWorkoutLog(log: WorkoutLog) = workoutDao.updateWorkoutLog(log)

    suspend fun deleteWorkoutLog(log: WorkoutLog) = workoutDao.deleteWorkoutLog(log)
}