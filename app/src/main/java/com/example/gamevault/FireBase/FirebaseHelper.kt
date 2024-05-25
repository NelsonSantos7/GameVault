package com.example.gamevault.firebase

import com.example.gamevault.model.Gamemodel
import com.example.gamevault.model.NoticiasModel
import com.example.gamevault.model.ReviewModel
import com.example.gamevault.model.Usermodel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseHelper {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun getNoticiasFromFirestore(): List<NoticiasModel> {
        val result = db.collection("noticias").get().await()
        return result.toObjects(NoticiasModel::class.java)
    }
    fun updateGameStatus(gameId: String, newStatus: Int): Task<Void> {
        return db.collection("games").document(gameId)
            .update("status", newStatus)
    }

    suspend fun getGamesFromFirestore(): List<Gamemodel> {
        val result = db.collection("games").get().await()
        return result.documents.map { it.toObject(Gamemodel::class.java)!! }
    }


    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun logoutUser() {
        auth.signOut()
    }

    suspend fun signInUser(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            auth.currentUser != null
        } catch (e: Exception) {
            false
        }
    }

    suspend fun checkUserExists(email: String): Boolean {
        val result = db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()
        return result.documents.isNotEmpty()
    }

    suspend fun addUserToFirestore(user: Usermodel) {
        val userId = user.id ?: throw IllegalArgumentException("User ID cannot be null")
        db.collection("users")
            .document(userId)
            .set(user)
            .await()
    }


    suspend fun getUserByEmail(email: String): Usermodel? {
        val result = db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()
        return if (result.documents.isNotEmpty()) {
            result.documents[0].toObject(Usermodel::class.java)
        } else {
            null
        }
    }

    suspend fun getGamesByStatusAndUser(status: Int, userId: String): List<Gamemodel> {
        val result = db.collection("games")
            .whereEqualTo("status", status)
            .whereEqualTo("userId", userId)
            .get()
            .await()
        return result.toObjects(Gamemodel::class.java)
    }

    suspend fun addGameToFirestore(game: Gamemodel) {
        db.collection("games").add(game).await()
    }


    suspend fun addReviewToFirestore(review: ReviewModel) {
        db.collection("reviews").add(review).await()
    }

    fun addReview(gameId: String, review: Map<String, Any>) = db.collection("games").document(gameId)
        .collection("reviews")
        .add(review)


}
