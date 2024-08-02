package com.example.todoapp.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.model.Task
import com.example.todoapp.database.TaskDatabase
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.viewmodel.TaskViewModel
import com.example.todoapp.viewmodel.TaskViewModelFactory
import com.example.todoapp.databinding.ActivityAddTaskBinding

class AddTaskActivity : AppCompatActivity() {

    private lateinit var taskViewModel: TaskViewModel

    private val binding: ActivityAddTaskBinding by lazy {
        ActivityAddTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        addTaskToolbar()
        setViewModel()
        clickEvents()
    }

    private fun addTaskToolbar(){
        val toolbar: Toolbar = binding.tbAddTask.tbApp
        setSupportActionBar(toolbar)
        toolbar.menu.clear()
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.title = "Add Task"
    }

    private fun setViewModel(){
        val dao = TaskDatabase.getDatabase(applicationContext).taskDao()
        val repository = TaskRepository(dao)
        taskViewModel = ViewModelProvider(
            this,
            TaskViewModelFactory(repository)
        )[TaskViewModel::class.java]
    }

    private fun clickEvents() {
        binding.btnAdd.setOnClickListener {
            val task =
                Task(binding.etAddTaskTitle.text.toString(), binding.etAddTaskDetail.text.toString())
            taskViewModel.addTask(task)
            Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
