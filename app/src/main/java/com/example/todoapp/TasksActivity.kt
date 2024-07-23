package com.example.todoapp

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var allTasksAdapter: AllTasksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val toolbar: Toolbar = binding.tbTask.tbApp
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        taskDetails()

        binding.fabAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }

        binding.opCompletedTasks.setOnClickListener {
            val intent = Intent(this, CompletedTasksActivity::class.java)
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
        tasksRecyclerView = binding.rvAllTasks
        tasksRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        allTasksAdapter = AllTasksAdapter(this)
        tasksRecyclerView.adapter = allTasksAdapter

        val dao = TaskDatabase.getDatabase(applicationContext).taskDao()
        val repository = TaskRepository(dao)
        taskViewModel = ViewModelProvider(
            this,
            AddTaskViewModelFactory(repository)
        )[TaskViewModel::class.java]

        taskViewModel.getTasks().observe(this, Observer { tasks ->
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
        AlertDialog.Builder(this).apply {
            setTitle("Delete Task")
            setMessage("Are you sure?")
            setPositiveButton("Yes") { _, _ ->
                taskViewModel.deleteTask(task)
                Toast.makeText(this@TasksActivity, "Task Deleted", Toast.LENGTH_SHORT).show()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            setCancelable(false)
        }.show()
    }

    override fun onCompleteClick(task: Task) {
        AlertDialog.Builder(this).apply {
            setTitle("Complete Task")
            setMessage("Are you sure?")
            setPositiveButton("Yes") { _, _ ->
                val updatedTask = task.copy(
                    isCompleted = true,
                    taskCompletionTime = System.currentTimeMillis()
                ) // Modify task as needed
                taskViewModel.editTask(updatedTask)
                Toast.makeText(this@TasksActivity, "Task Completed", Toast.LENGTH_SHORT).show()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            setCancelable(false)
        }.show()
    }

}