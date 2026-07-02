package com.example.habitstracker.data.dao

import androidx.room.*
import com.example.habitstracker.data.entity.WorkoutLog
import com.example.habitstracker.data.entity.WorkoutType
import com.example.habitstracker.data.model.WorkoutSummary

import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    // WorkoutType

    @Insert
    suspend fun insertWorkoutType(type: WorkoutType): Long

    @Query("SELECT * FROM workout_types")
    fun getAllWorkoutTypes(): Flow<List<WorkoutType>>

    // WorkoutLog

    @Insert
    suspend fun insertWorkoutLog(log: WorkoutLog): Long

    @Update
    suspend fun updateWorkoutLog(log: WorkoutLog)

    @Delete
    suspend fun deleteWorkoutLog(log: WorkoutLog)

    @Query("""
        SELECT * FROM workout_logs
        ORDER BY date DESC
    """)
    fun getAllWorkoutLogs(): Flow<List<WorkoutLog>>

    @Query("""
        SELECT * FROM workout_logs
        WHERE logId = :logId
    """)
    fun getWorkoutLogById(
        logId: Long
    ): Flow<WorkoutLog?>

    @Query("""
    SELECT *
    FROM workout_logs
    ORDER BY date DESC
    LIMIT 1
    """)
    fun getLatestWorkout(): Flow<WorkoutLog?>

    @Query("""
    SELECT
        wt.name AS name,
        wl.date AS date,
        wl.duration AS duration,
        wl.calories AS calories
    FROM workout_logs wl
    INNER JOIN workout_types wt
    ON wl.typeId = wt.typeId
    ORDER BY wl.date DESC
    LIMIT 1
    """)
    fun getLatestWorkoutSummary(): Flow<WorkoutSummary?>
}
