package com.example.habitstracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(

    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,

    val name: String,

    val gender: String,

    val birthDate: String,

    val height: Float,

    val avatar: String?,

    val createdAt: String
)
