package com.example.gamevault.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamevault.databinding.ActivityAddGameBinding
import com.example.gamevault.firebase.FirebaseHelper
import com.example.gamevault.model.Gamemodel

class AddGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebaseHelper = FirebaseHelper()

        binding.buttonAddGame.setOnClickListener {
            val title = binding.editTextTitle.text.toString().trim()
            val distributor = binding.editTextDistributor.text.toString().trim()
            val year = binding.editTextYear.text.toString().toIntOrNull()
            val photoUrl = binding.editTextPhotoUrl.text.toString().trim()
            val trailerUrl = binding.editTextTrailerUrl.text.toString().trim()
            val summary = binding.editTextSummary.text.toString().trim()
            val estimatedTime = binding.editTextEstimatedTime.text.toString().toIntOrNull()
            val platforms = binding.editTextPlatforms.text.toString().trim()

            if (title.isEmpty() || distributor.isEmpty() || year == null || photoUrl.isEmpty() || trailerUrl.isEmpty() || summary.isEmpty() || estimatedTime == null || platforms.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newGame = Gamemodel(
                titulo = title,
                distribuidora = distributor,
                anoLancamento = year,
                fotoUrl = photoUrl,
                miniTrailer = trailerUrl,
                resumo = summary,
                tempoEstimado = estimatedTime,
                plataformas = platforms,
                status = 0 // Default status
            )

            firebaseHelper.addGameToFirestore(newGame,
                onSuccess = {
                    Toast.makeText(this, "Jogo adicionado com sucesso.", Toast.LENGTH_SHORT).show()
                    finish()
                },
                onFailure = { exception ->
                    Toast.makeText(this, "Erro ao adicionar jogo: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
