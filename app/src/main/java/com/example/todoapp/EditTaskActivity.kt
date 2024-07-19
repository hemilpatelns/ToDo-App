package com.example.todoapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.databinding.ActivityEditTaskBinding

class EditTaskActivity : AppCompatActivity() {

    private lateinit var addTaskViewModel: AddTaskViewModel
    private var taskId: Int = 0

    private val binding: ActivityEditTaskBinding by lazy {
        ActivityEditTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        editTaskToolbar()

        val dao = TaskDatabase.getDatabase(applicationContext).taskDao()
        val repository = TaskRepository(dao)
        addTaskViewModel = ViewModelProvider(
            this,
            AddTaskViewModelFactory(repository)
        )[AddTaskViewModel::class.java]

        taskId = intent.getIntExtra("id", 0)
        addTaskViewModel.getTaskById(taskId).observe(this, Observer { task ->
            task?.let {
                binding.editTaskTitle.setText(it.taskTitle)
                binding.editTaskDetail.setText(it.taskSubtitle)
            }
        })

        binding.updateBtn.setOnClickListener {

            AlertDialog.Builder(this).apply {
                setTitle("Save Changes")
                setMessage("Are you sure?")
                setPositiveButton("Yes") { _, _ ->
                    val task =
                        Task(
                            binding.editTaskTitle.text.toString(),
                            binding.editTaskDetail.text.toString(),
                            taskId
                        )
                    addTaskViewModel.editTask(task)
                    Toast.makeText(this@EditTaskActivity, "Task Updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
                setNeutralButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                setCancelable(false)
            }.show()
        }

        binding.cancelBtn.setOnClickListener {
            finish()
        }
    }

    private fun editTaskToolbar() {
        val toolbar: Toolbar = binding.toolbarEditTask.appToolbar
        setSupportActionBar(toolbar)
        toolbar.menu.clear()
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.title = "Edit Task"
    }
}