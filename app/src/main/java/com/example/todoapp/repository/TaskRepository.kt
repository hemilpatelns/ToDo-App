package com.example.todoapp.repository

import androidx.lifecycle.LiveData
import com.example.todoapp.database.TaskDao
import com.example.todoapp.model.Task

class TaskRepository(private val taskDao: TaskDao) {

    fun getTasks(): LiveData<List<Task>> {
        return taskDao.getTasks()
    }

    fun getTaskById(id: Int): LiveData<Task> {
        return taskDao.getTaskById(id)
    }

    fun getCompletedTasks(): LiveData<List<Task>> {
        return taskDao.getCompletedTasks()
    }

    suspend fun addTask(task: Task) {
        taskDao.addTask(task)
    }

    suspend fun editTask(task: Task) {
        taskDao.editTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }
}