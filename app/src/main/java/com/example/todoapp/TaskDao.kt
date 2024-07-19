package com.example.todoapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Query(
        "SELECT * FROM tasks ORDER BY CASE WHEN iscompleted = 0 THEN id ELSE NULL END DESC, CASE WHEN iscompleted = 1 THEN id ELSE NULL END DESC"
    )
    fun getTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted=1")
    fun getCompletedTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE id =:id")
    fun getTaskById(id: Int): LiveData<Task>

    @Insert
    suspend fun addTask(task: Task)

    @Update
    suspend fun editTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}