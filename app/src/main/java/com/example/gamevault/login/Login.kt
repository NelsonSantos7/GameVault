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

        checkLoginStatus()

        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {
            btnLogin.setOnClickListener {
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()
                if (authenticateUser(username, password)) {
                    saveLoginStatus(chkKeepLoggedIn.isChecked, username)
                    navigateToMainActivity()
                } else {
                    Toast.makeText(this@Login, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }

            btnRegister.setOnClickListener {
                startActivity(Intent(this@Login, Register::class.java))
            }
        }
    }

    private fun authenticateUser(username: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Usuarios WHERE username=? AND password=?", arrayOf(username, password))
        val isAuthenticated = cursor.count > 0
        cursor.close()
        return isAuthenticated
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun saveLoginStatus(isKeepLoggedIn: Boolean, username: String) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("KEEP_LOGGED_IN", isKeepLoggedIn)
        if (isKeepLoggedIn) {
            editor.putString("USERNAME", username)
        }
        editor.apply()
    }

    private fun checkLoginStatus() {
        val isKeepLoggedIn = sharedPreferences.getBoolean("KEEP_LOGGED_IN", false)
        if (isKeepLoggedIn) {
            navigateToMainActivity()
        }
    }
}
