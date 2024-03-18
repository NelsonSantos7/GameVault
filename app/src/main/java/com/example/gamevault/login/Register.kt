package com.example.gamevault.login

import android.content.ContentValues
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamevault.SQLite.DBhelper
import com.example.gamevault.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbHelper: DBhelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBhelper(this) // Inicialize o dbHelper aqui

        binding.btnRegisterReg.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val username = binding.etUsernameReg.text.toString()
        val password = binding.etPasswordReg.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Verifique se as senhas coincidem
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        // Verifica se a senha tem pelo menos 5 caracteres e contém pelo menos um caractere especial
        if (password.length < 5 || !password.contains(Regex("[^A-Za-z0-9]"))) {
            Toast.makeText(this, "Password must be at least 5 characters long and contain at least one special character", Toast.LENGTH_LONG).show()
            return
        }

        if (!checkUserExists(username)) {
            val contentValues = ContentValues().apply {
                put("username", username)
                put("password", password)
            }
            val db = dbHelper.writableDatabase
            db.insert("Usuarios", null, contentValues)

            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkUserExists(username: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Usuarios WHERE username=?", arrayOf(username))
        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }
}
