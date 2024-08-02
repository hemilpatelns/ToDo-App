package com.example.todoapp.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.view.adapter.AllTasksAdapter
import com.example.todoapp.model.Task
import com.example.todoapp.view.adapter.TaskActionListener
import com.example.todoapp.database.TaskDatabase
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.viewmodel.TaskViewModel
import com.example.todoapp.viewmodel.TaskViewModelFactory
import com.example.todoapp.databinding.ActivityCompletedTasksBinding

class CompletedTasksActivity : AppCompatActivity() {

    private val binding: ActivityCompletedTasksBinding by lazy {
        ActivityCompletedTasksBinding.inflate(layoutInflater)
    }

    private lateinit var completedTaskRecyclerView: RecyclerView
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var completedTasksAdapter: AllTasksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        completedTasksToolbar()
        completedTaskDetails()
    }

    private fun completedTasksToolbar(){
        val toolbar: Toolbar = binding.tbCompletedTasks.tbApp
        setSupportActionBar(toolbar)
        toolbar.menu.clear()
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.title = "Completed Tasks"
    }

    private fun completedTaskDetails() {
        completedTaskRecyclerView = binding.rvCompletedTasks
        completedTaskRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        completedTasksAdapter = AllTasksAdapter(listener = object : TaskActionListener {
            override fun onEditClick(task: Task) {

            }

            override fun onDeleteClick(task: Task) {
                AlertDialog.Builder(this@CompletedTasksActivity).apply {
                    setTitle("Delete Task")
                    setMessage("Are you sure?")
                    setPositiveButton("Yes") { _, _ ->
                        taskViewModel.deleteTask(task)
                        Toast.makeText(this@CompletedTasksActivity, "Task Deleted", Toast.LENGTH_SHORT).show()
                    }
                    setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    setCancelable(false)
                }.show()
            }

            override fun onCompleteClick(task: Task) {

            }

        })
        completedTaskRecyclerView.adapter = completedTasksAdapter

        val dao = TaskDatabase.getDatabase(applicationContext).taskDao()
        val repository = TaskRepository(dao)
        taskViewModel = ViewModelProvider(
            this,
            TaskViewModelFactory(repository)
        )[TaskViewModel::class.java]

        taskViewModel.getCompletedTasks().observe(this){
            completedTasksAdapter.setTasks(it)
        }
    }
}