package com.example.gamevault.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gamevault.SQLite.DBhelper
import com.example.gamevault.databinding.ActivityRegisterBinding
import com.example.gamevault.model.Usermodel
import com.google.android.material.snackbar.Snackbar

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

        if (!validateInput(username, email, password, confirmPassword)) return

        if (dbHelper.checkUserExists(email, username)) {
            showSnackbar("E-mail ou nome de usuário já registrado.")
            return
        }

        val hashedPassword = hashPassword(password)
        val user = Usermodel(username = username, email = email, password = hashedPassword)

        if (dbHelper.addUser(user) > 0) {
            showSnackbar("Registro realizado com sucesso.")
            finish()
        } else {
            showSnackbar("Erro ao registrar usuário. Tente novamente.")
        }
    }

    private fun validateInput(username: String, email: String, password: String, confirmPassword: String): Boolean {
        return when {
            username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                showSnackbar("Todos os campos devem ser preenchidos.")
                false
            }
            password != confirmPassword -> {
                showSnackbar("As senhas não coincidem.")
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showSnackbar("Forneça um endereço de e-mail válido.")
                false
            }
            else -> true
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun hashPassword(password: String): String {
        return password
    }
}
