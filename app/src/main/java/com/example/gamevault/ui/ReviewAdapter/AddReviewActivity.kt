package com.example.gamevault.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamevault.SQLite.DBhelper
import com.example.gamevault.databinding.ActivityAddReviewBinding
import com.example.gamevault.model.ReviewModel

class AddReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddReviewBinding
    private lateinit var dbHelper: DBhelper
    private var gameId: Int = -1
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBhelper(this)

        // Recebe os IDs do jogo e do usuário da Intent
        gameId = intent.getIntExtra("GAME_ID", -1)
        userId = intent.getIntExtra("USER_ID", -1)

        if (gameId == -1 || userId == -1) {
            Toast.makeText(this, "Dados inválidos", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.buttonSubmitReview.setOnClickListener { submitReview() }
    }

    private fun submitReview() {
        val rating = binding.ratingBar.rating.toInt()
        val comment = binding.editTextComment.text.toString().trim()

        if (comment.isEmpty()) {
            Toast.makeText(this, "Por favor, escreva um comentário.", Toast.LENGTH_SHORT).show()
            return
        }

        val review = ReviewModel(
            id = null,  // Altere aqui se necessário
            gameId = gameId,
            userId = userId,
            rating = rating,
            comment = comment
        )

        val result = dbHelper.addReview(review)
        if (result > 0) {
            Toast.makeText(this, "Avaliação enviada com sucesso.", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Erro ao enviar avaliação.", Toast.LENGTH_SHORT).show()
        }
    }
}
