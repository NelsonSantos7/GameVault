package com.example.gamevault.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamevault.databinding.ActivityAddUserBinding
import com.example.gamevault.firebase.FirebaseHelper
import com.example.gamevault.model.Usermodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUserBinding
    private val firebaseHelper = FirebaseHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAddUser.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val username = binding.editTextUsername.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newUser = Usermodel(email = email, password = password, username = username)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    firebaseHelper.addUserToFirestore(newUser)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AddUserActivity, "Usuário adicionado com sucesso.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AddUserActivity, "Erro ao adicionar usuário: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
