package com.example.todoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    val taskTitle: String,
    val taskSubtitle:String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val taskCompletionTime: Long? = null,
    val isCompleted: Boolean = false
)