package com.example.gamevault.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamevault.databinding.ActivityAddUserBinding
import com.example.gamevault.firebase.FirebaseHelper
import com.example.gamevault.model.Usermodel
import com.google.firebase.auth.FirebaseAuth
import org.mindrot.jbcrypt.BCrypt

class AddUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUserBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebaseHelper = FirebaseHelper()
        auth = FirebaseAuth.getInstance()

        binding.buttonAddUser.setOnClickListener {
            val username = binding.editTextUsername.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Por favor, forneça um e-mail válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())

            val newUser = Usermodel(
                username = username,
                email = email,
                password = hashedPassword // Hash da senha
            )

            firebaseHelper.addUserToFirestore(newUser,
                onSuccess = {
                    Toast.makeText(this, "Usuário adicionado com sucesso.", Toast.LENGTH_SHORT).show()
                    finish()
                },
                onFailure = { exception ->
                    Toast.makeText(this, "Erro ao adicionar usuário: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
