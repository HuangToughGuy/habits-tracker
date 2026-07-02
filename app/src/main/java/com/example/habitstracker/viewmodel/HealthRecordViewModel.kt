package com.example.habitstracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.data.entity.HealthRecord
import com.example.habitstracker.data.repository.HealthRecordRepository
import kotlinx.coroutines.launch

class HealthRecordViewModel(
    private val repository: HealthRecordRepository
) : ViewModel() {

    val records = repository.getAllRecords()

    val latestHealth = repository.getLatestRecord()
    fun getRecordById(
        recordId: Long
    ) = repository.getRecordById(recordId)

    suspend fun insertRecord(
        record: HealthRecord
    ): Long {

        return repository.insertRecord(record)
    }

    fun updateRecord(
        record: HealthRecord
    ) {

        viewModelScope.launch {

            repository.updateRecord(record)
        }
    }

    fun deleteRecord(
        record: HealthRecord
    ) {
        viewModelScope.launch {
            repository.deleteRecord(record)
        }
    }
}