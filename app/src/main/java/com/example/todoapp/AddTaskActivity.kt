package com.example.todoapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
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

        val dao = TaskDatabase.getDatabase(applicationContext).taskDao()
        val repository = TaskRepository(dao)
        taskViewModel = ViewModelProvider(
            this,
            AddTaskViewModelFactory(repository)
        )[TaskViewModel::class.java]

        binding.btnAdd.setOnClickListener {
            val task =
                Task(binding.etAddTaskTitle.text.toString(), binding.etAddTaskDetail.text.toString())
            taskViewModel.addTask(task)
            Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show()
            finish()
        }
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
}