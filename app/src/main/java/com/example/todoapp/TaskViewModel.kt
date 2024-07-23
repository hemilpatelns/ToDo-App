package com.example.todoapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository): ViewModel() {

    fun getTasks(): LiveData<List<Task>>{
        return taskRepository.getTasks()
    }

    fun getTaskById(id:Int):LiveData<Task>{
        return taskRepository.getTaskById(id)
    }

    fun getCompletedTasks(): LiveData<List<Task>>{
        return taskRepository.getCompletedTasks()
    }

    fun addTask(task: Task){
        viewModelScope.launch(Dispatchers.IO){
            taskRepository.addTask(task)
        }
    }

    fun editTask(task: Task){
        viewModelScope.launch(Dispatchers.IO){
            taskRepository.editTask(task)
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch(Dispatchers.IO){
            taskRepository.deleteTask(task)
        }
    }
}