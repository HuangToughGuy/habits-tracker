package com.example.habitstracker.data.dao

import androidx.room.*
import com.example.habitstracker.data.entity.HealthRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthRecordDao {

    @Insert
    suspend fun insertRecord(record: HealthRecord): Long

    @Update
    suspend fun updateRecord(record: HealthRecord)

    @Delete
    suspend fun deleteRecord(record: HealthRecord)

    @Query("""
        SELECT * FROM health_records
        ORDER BY date DESC
    """)
    fun getAllRecords(): Flow<List<HealthRecord>>

    @Query("""
    SELECT * FROM health_records
    WHERE recordId = :recordId
    """)
    fun getRecordById(
        recordId: Long
    ): Flow<HealthRecord?>
}