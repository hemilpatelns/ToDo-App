package com.example.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ActivityCompletedTasksBinding

class CompletedTasksActivity : AppCompatActivity() {

    private val binding: ActivityCompletedTasksBinding by lazy {
        ActivityCompletedTasksBinding.inflate(layoutInflater)
    }

    private lateinit var completedTaskRecyclerView: RecyclerView
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var completedTasksAdapter: CompletedTaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        completedTaskToolbar()

        completedTaskDetails()

    }

    private fun completedTaskToolbar(){
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
        completedTasksAdapter = CompletedTaskAdapter()
        completedTaskRecyclerView.adapter = completedTasksAdapter

        val dao = TaskDatabase.getDatabase(applicationContext).taskDao()
        val repository = TaskRepository(dao)
        taskViewModel = ViewModelProvider(
            this,
            AddTaskViewModelFactory(repository)
        )[TaskViewModel::class.java]

        taskViewModel.getCompletedTasks().observe(this, Observer { tasks ->
            tasks?.let {
                completedTasksAdapter.setCompletedTasks(it)
            }
        })
    }
}