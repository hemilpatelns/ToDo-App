package com.example.todoapp

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ActivityTasksBinding

class TasksActivity : AppCompatActivity(), TaskActionListener {

    private val binding: ActivityTasksBinding by lazy {
        ActivityTasksBinding.inflate(layoutInflater)
    }

    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var addTaskViewModel: AddTaskViewModel
    private lateinit var allTasksAdapter: AllTasksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val toolbar: Toolbar = binding.toolbarTask.appToolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        taskDetails()

        binding.addTaskFab.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bottom_navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_calendar -> {
                showCalendar()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showCalendar() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Handle date selection (if needed)
                Toast.makeText(
                    this,
                    "Selected Date: $selectedDay/${selectedMonth + 1}/$selectedYear",
                    Toast.LENGTH_SHORT
                ).show()
            }, year, month, day
        )

        datePickerDialog.show()
    }

    private fun taskDetails() {
        tasksRecyclerView = binding.recyclerAllTasks
        tasksRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        allTasksAdapter = AllTasksAdapter(this)
        tasksRecyclerView.adapter = allTasksAdapter

        val dao = TaskDatabase.getDatabase(applicationContext).taskDao()
        val repository = TaskRepository(dao)
        addTaskViewModel = ViewModelProvider(
            this,
            AddTaskViewModelFactory(repository)
        )[AddTaskViewModel::class.java]

        addTaskViewModel.getTasks().observe(this, Observer { tasks ->
            tasks?.let {
                allTasksAdapter.setTasks(it)
            }
        })
    }

    override fun onEditClick(task: Task) {
        val intent = Intent(this, EditTaskActivity::class.java)
        intent.putExtra("id", task.id)
        startActivity(intent)
    }

    override fun onDeleteClick(task: Task) {
        addTaskViewModel.deleteTask(task)
    }

    override fun onCompleteClick(task: Task) {
        val updatedTask = task.copy(isCompleted = true) // Modify task as needed
        addTaskViewModel.editTask(updatedTask)
    }

}