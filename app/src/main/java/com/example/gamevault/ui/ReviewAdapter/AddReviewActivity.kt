package com.example.gamevault.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gamevault.databinding.ActivityAddReviewBinding
import com.example.gamevault.firebase.FirebaseHelper
import com.example.gamevault.model.ReviewModel
import kotlinx.coroutines.launch

class AddReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddReviewBinding
    private lateinit var firebaseHelper: FirebaseHelper
    private var gameId: String = ""
    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseHelper = FirebaseHelper()

        gameId = intent.getStringExtra("GAME_ID") ?: ""
        userId = intent.getStringExtra("USER_ID") ?: ""

        if (gameId.isEmpty() || userId.isEmpty()) {
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
            id = null,
            gameId = gameId,
            userId = userId,
            rating = rating,
            comment = comment
        )

        lifecycleScope.launch {
            try {
                firebaseHelper.addReviewToFirestore(review)
                Toast.makeText(this@AddReviewActivity, "Avaliação enviada com sucesso.", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@AddReviewActivity, "Erro ao enviar avaliação: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
