package com.example.habitstracker.data.dao

import androidx.room.*
import com.example.habitstracker.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM users LIMIT 1")
    fun getCurrentUser(): Flow<User?>

    @Query("""
    SELECT * FROM users
    WHERE userId = :userId
    """)
    fun getUserById(
        userId: Long
    ): Flow<User?>

    @Query("""
    SELECT COUNT(*) FROM users
    """)
    suspend fun getUserCount(): Int
}