package com.example.gamevault.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamevault.MainActivity
import com.example.gamevault.SQLite.DBhelper
import com.example.gamevault.databinding.ActivityLoginBinding
import com.example.gamevault.model.Usermodel

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper: DBhelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBhelper(this)
        sharedPreferences = getSharedPreferences("com.example.gamevault.prefs", MODE_PRIVATE)

        setupListeners()
        checkLoginStatus()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username and password must not be empty", Toast.LENGTH_SHORT).show()
            } else {
                authenticateUser(username, password)
            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this@Login, Register::class.java))
        }
    }

    private fun authenticateUser(username: String, password: String) {
        val user = dbHelper.getUserByUsername(username)
        user?.let { usermodel ->
            if (dbHelper.checkPasswordHash(username, password)) {
                saveLoginStatus(usermodel)
                navigateToMainActivity()
            } else {
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveLoginStatus(user: Usermodel) {
        with(sharedPreferences.edit()) {
            putInt("USER_ID", user.id)
            putString("USER_EMAIL", user.email)
            putString("USERNAME", user.username)
            apply()
        }
    }

    private fun checkLoginStatus() {
        val userId = sharedPreferences.getInt("USER_ID", -1)
        if (userId != -1) {
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }
}
