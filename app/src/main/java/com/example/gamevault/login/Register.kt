package com.example.gamevault.login

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamevault.SQLite.DBhelper
import com.example.gamevault.databinding.ActivityRegisterBinding
import com.example.gamevault.firebase.FirebaseHelper
import com.example.gamevault.model.Usermodel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.mindrot.jbcrypt.BCrypt

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbHelper: DBhelper
    private lateinit var firebaseHelper: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbHelper = DBhelper(this)
        firebaseHelper = FirebaseHelper()

        binding.buttonRegister.setOnClickListener {
            attemptRegister()
        }
    }

    private fun attemptRegister() {
        val username = binding.editTextUsername.text.toString().trim()
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Por favor, forneça um e-mail válido.", Toast.LENGTH_SHORT).show()
            return
        }

        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Register user in Firebase Authentication
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user

                // Check if user registration was successful
                if (firebaseUser != null) {
                    val newUser = Usermodel(
                        id = firebaseUser.uid,
                        username = username,
                        email = email,
                        password = hashedPassword
                    )

                    // Add user to Firebase Firestore
                    firebaseHelper.addUserToFirestoreSuspend(newUser)

                    // Add user to SQLite Database
                    val values = ContentValues().apply {
                        put(DBhelper.COLUMN_USER_EMAIL, email)
                        put(DBhelper.COLUMN_USER_PASSWORD, hashedPassword)
                        put(DBhelper.COLUMN_USER_USERNAME, username)
                    }
                    dbHelper.writableDatabase.insert(DBhelper.TABLE_USERS, null, values)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@Register, "Usuário registrado com sucesso.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@Register, Login::class.java))
                        finish()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@Register, "Falha ao registrar usuário.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Register, "Erro ao registrar usuário: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
