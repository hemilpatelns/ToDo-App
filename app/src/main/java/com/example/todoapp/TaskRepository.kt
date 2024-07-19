package com.example.todoapp

import androidx.lifecycle.LiveData

class TaskRepository(private val taskDao: TaskDao) {

    fun getTasks(): LiveData<List<Task>> {
        return taskDao.getTasks()
    }

    fun getTaskById(id: Int): LiveData<Task> {
        return taskDao.getTaskById(id)
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