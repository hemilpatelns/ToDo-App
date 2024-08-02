package com.example.todoapp.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.model.Task
import com.example.todoapp.database.TaskDatabase
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.viewmodel.TaskViewModel
import com.example.todoapp.viewmodel.TaskViewModelFactory
import com.example.todoapp.databinding.ActivityEditTaskBinding

class EditTaskActivity : AppCompatActivity() {

    private lateinit var taskViewModel: TaskViewModel
    private var taskId: Int = 0

    private val binding: ActivityEditTaskBinding by lazy {
        ActivityEditTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        editTaskToolbar()
        setViewModel()
        setObserver()
        clickEvents()
    }

    private fun editTaskToolbar() {
        val toolbar: Toolbar = binding.tbEditTask.tbApp
        setSupportActionBar(toolbar)
        toolbar.menu.clear()
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.title = "Edit Task"
    }

    private fun setViewModel() {
        val dao = TaskDatabase.getDatabase(applicationContext).taskDao()
        val repository = TaskRepository(dao)
        taskViewModel = ViewModelProvider(
            this,
            TaskViewModelFactory(repository)
        )[TaskViewModel::class.java]
    }

    private fun setObserver() {
        taskId = intent.getIntExtra("id", 0)
        taskViewModel.getTaskById(taskId).observe(this) { task ->
            task?.let {
                binding.etEditTaskTitle.setText(it.taskTitle)
                binding.etEditTaskDetail.setText(it.taskSubtitle)
            }
        }
    }

    private fun clickEvents() {
        binding.btnUpdate.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Save Changes")
                setMessage("Are you sure?")
                setPositiveButton("Yes") { _, _ ->
                    val task =
                        Task(
                            binding.etEditTaskTitle.text.toString(),
                            binding.etEditTaskDetail.text.toString(),
                            taskId
                        )
                    taskViewModel.editTask(task)
                    Toast.makeText(this@EditTaskActivity, "Task Updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
                setNeutralButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                setCancelable(false)
            }.show()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }
}