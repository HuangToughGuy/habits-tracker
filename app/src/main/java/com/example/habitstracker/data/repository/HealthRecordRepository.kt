package com.example.habitstracker.data.repository

import com.example.habitstracker.data.dao.HealthRecordDao
import com.example.habitstracker.data.entity.HealthRecord

class HealthRecordRepository(
    private val healthRecordDao: HealthRecordDao
) {

    fun getAllRecords() =
        healthRecordDao.getAllRecords()

    suspend fun insertRecord(record: HealthRecord) =
        healthRecordDao.insertRecord(record)

    suspend fun updateRecord(record: HealthRecord) =
        healthRecordDao.updateRecord(record)

    suspend fun deleteRecord(record: HealthRecord) =
        healthRecordDao.deleteRecord(record)
}