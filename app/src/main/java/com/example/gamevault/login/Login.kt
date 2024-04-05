package com.example.gamevault.login

import android.annotation.SuppressLint
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
        checkLoginStatus()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            authenticateUser(username, password)
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this@Login, Register::class.java))
        }
    }

    @SuppressLint("Range")
    private fun authenticateUser(username: String, password: String) {
        val userCursor = dbHelper.getUserByUsername(username)
        if (userCursor.moveToFirst() && userCursor.getString(userCursor.getColumnIndex(DBhelper.COLUMN_PASSWORD)) == password) {
            val userId = userCursor.getInt(userCursor.getColumnIndex(DBhelper.COLUMN_USER_ID))
            val userEmail = userCursor.getString(userCursor.getColumnIndex(DBhelper.COLUMN_EMAIL))
            val userName = userCursor.getString(userCursor.getColumnIndex(DBhelper.COLUMN_USERNAME))
            saveLoginStatus(userId, userEmail, userName)
            navigateToMainActivity()
        } else {
            userCursor.close()
            Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
        }
    }



    private fun saveLoginStatus(userId: Int, userEmail: String, userName: String) { // Modificado para incluir userName
        with(sharedPreferences.edit()) {
            putInt("USER_ID", userId)
            putString("USER_EMAIL", userEmail)
            putString("USERNAME", userName)
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
