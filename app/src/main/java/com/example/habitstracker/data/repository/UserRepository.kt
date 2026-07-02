package com.example.habitstracker.data.repository

import com.example.habitstracker.data.dao.UserDao
import com.example.habitstracker.data.entity.User

class UserRepository(
    private val userDao: UserDao
) {

    fun getCurrentUser() = userDao.getCurrentUser()

    fun getUserById(userId: Long) = userDao.getUserById(userId)

    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun updateUser(user: User) = userDao.updateUser(user)

    suspend fun deleteUser(user: User) = userDao.deleteUser(user)

    suspend fun getUserCount() = userDao.getUserCount()
}