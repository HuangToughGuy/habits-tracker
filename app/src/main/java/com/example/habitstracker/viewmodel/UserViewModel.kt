package com.example.habitstracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.data.entity.User
import com.example.habitstracker.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    val currentUser = repository.getCurrentUser()

    suspend fun insertUser(
        user: User
    ): Long {

        return repository.insertUser(user)
    }

    fun updateUser(
        user: User
    ) {

        viewModelScope.launch {

            repository.updateUser(user)
        }
    }

    fun deleteUser(
        user: User
    ) {

        viewModelScope.launch {

            repository.deleteUser(user)
        }
    }

    suspend fun getUserCount(): Int {

        return repository.getUserCount()
    }
    fun getUserById(userId: Long) = repository.getUserById(userId)
}