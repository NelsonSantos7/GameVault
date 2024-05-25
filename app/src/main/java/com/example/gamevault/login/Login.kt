package com.example.gamevault.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gamevault.MainActivity
import com.example.gamevault.databinding.ActivityLoginBinding
import com.example.gamevault.firebase.FirebaseHelper
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseHelper: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseHelper = FirebaseHelper()
        sharedPreferences = getSharedPreferences("com.example.gamevault.prefs", MODE_PRIVATE)

        if (isLoggedIn()) {
            navigateToMainActivity()
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Por favor, forneça um e-mail válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val success = firebaseHelper.signInUser(email, password)
                if (success) {
                    val user = firebaseHelper.getUserByEmail(email)
                    if (user != null) {
                        saveLoginStatus(user.username, email)
                        navigateToMainActivity()
                    } else {
                        Toast.makeText(this@Login, "Falha ao carregar dados do usuário.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Login, "Falha na autenticação.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this@Login, Register::class.java))
        }
    }

    private fun saveLoginStatus(username: String, email: String) {
        with(sharedPreferences.edit()) {
            putString("USERNAME", username)
            putString("USER_EMAIL", email)
            apply()
        }
        Log.d("Login", "Saved USERNAME: $username and USER_EMAIL: $email to SharedPreferences")
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    private fun isLoggedIn(): Boolean {
        return sharedPreferences.contains("USER_EMAIL")
    }
}
