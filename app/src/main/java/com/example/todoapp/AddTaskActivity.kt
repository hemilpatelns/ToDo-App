package com.example.todoapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.databinding.ActivityAddTaskBinding

class AddTaskActivity : AppCompatActivity() {

    private lateinit var addTaskViewModel: AddTaskViewModel

    private val binding: ActivityAddTaskBinding by lazy {
        ActivityAddTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        addTaskToolbar()

        val dao = TaskDatabase.getDatabase(applicationContext).taskDao()
        val repository = TaskRepository(dao)
        addTaskViewModel = ViewModelProvider(
            this,
            AddTaskViewModelFactory(repository)
        )[AddTaskViewModel::class.java]

        binding.addBtn.setOnClickListener {
            val task =
                Task(binding.addTaskTitle.text.toString(), binding.addTaskDetail.text.toString())
            addTaskViewModel.addTask(task)
            Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun addTaskToolbar(){
        val toolbar: Toolbar = binding.toolbarAddTask.appToolbar
        setSupportActionBar(toolbar)
        toolbar.menu.clear()

        toolbar.setNavigationOnClickListener {
            finish()
        }

        toolbar.title = "Add Task"
    }
}