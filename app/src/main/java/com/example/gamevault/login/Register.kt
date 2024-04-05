package com.example.gamevault.login

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamevault.SQLite.DBhelper
import com.example.gamevault.SQLite.Usermodel
import com.example.gamevault.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbHelper: DBhelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = DBhelper(this)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnRegisterReg.setOnClickListener {
            attemptRegister()
        }
    }

    private fun attemptRegister() {
        val username = binding.etUsernameReg.text.toString().trim()
        val email = binding.etEmailReg.text.toString().trim()
        val password = binding.etPasswordReg.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        // Validation
        if (!validateInput(username, email, password, confirmPassword)) return

        // Check for existing user
        if (dbHelper.checkUserExists(email, username)) {
            showToast("E-mail ou nome de usuário já registrado.")
            return
        }

        // Attempt to register
        registerUser(Usermodel(username = username, email = email, password = password))
    }

    private fun validateInput(username: String, email: String, password: String, confirmPassword: String): Boolean {
        return when {
            username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                showToast("Todos os campos devem ser preenchidos.")
                false
            }
            password != confirmPassword -> {
                showToast("As senhas não coincidem.")
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showToast("Forneça um endereço de e-mail válido.")
                false
            }
            else -> true
        }
    }

    private fun registerUser(user: Usermodel) {
        if (dbHelper.addUser(user) > 0) {
            showToast("Registro realizado com sucesso.")
            finish()
        } else {
            showToast("Erro ao registrar usuário. Tente novamente.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
