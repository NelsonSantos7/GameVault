package com.example.gamevault.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamevault.MainActivity
import com.example.gamevault.SQLite.DBhelper
import com.example.gamevault.databinding.ActivityLoginBinding

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
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (dbHelper.checkPasswordHash(username, password)) {
                dbHelper.getUserByUsername(username)?.let {
                    saveLoginStatus(it.id!!, it.email, it.username)
                    navigateToMainActivity()
                }
            } else {
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this@Login, Register::class.java))
        }
    }

    private fun saveLoginStatus(userId: Int, userEmail: String, userName: String) {
        with(sharedPreferences.edit()) {
            putInt("USER_ID", userId)
            putString("USER_EMAIL", userEmail)
            putString("USERNAME", userName)
            apply()
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }
}
