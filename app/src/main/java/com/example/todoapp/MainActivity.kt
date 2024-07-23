package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.todoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.tbLogin.tbApp
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        toolbar.menu.clear()

        // Login on click event
        binding.btnLogin.setOnClickListener {
            authenticateUser()
        }
    }

    private fun authenticateUser() {
        if (binding.etEmail.text.toString() == Authentication.email && binding.etPassword.text.toString() == Authentication.password) {
            Toast.makeText(this,"Authentication Success", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TasksActivity::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
        }
    }

    object Authentication {
        val email = "hem"
        val password = "hem"
    }
}