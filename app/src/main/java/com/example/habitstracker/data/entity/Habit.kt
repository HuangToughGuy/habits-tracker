package com.example.habitstracker.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "habits",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]

)
data class Habit(

    @PrimaryKey(autoGenerate = true)
    val habitId: Long = 0,

    val userId: Long,

    val name: String,

    val description: String,

    val category: String,

    val target: Int,

    val icon: String,

    val color: String,

    val createdDate: String,

    val isActive: Boolean = true
)