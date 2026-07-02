package com.example.habitstracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habitstracker.data.repository.UserRepository

class UserViewModelFactory(
    private val repository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                UserViewModel::class.java
            )
        ) {

            @Suppress("UNCHECKED_CAST")

            return UserViewModel(
                repository
            ) as T
        }
        throw IllegalArgumentException()
    }
}