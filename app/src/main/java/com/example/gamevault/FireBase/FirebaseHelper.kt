package com.example.gamevault.firebase

import com.example.gamevault.model.Gamemodel
import com.example.gamevault.model.ReviewModel
import com.example.gamevault.model.Usermodel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseHelper {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun addGameToFirestore(game: Gamemodel, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("games")
            .add(game)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getGamesFromFirestore(onSuccess: (List<Gamemodel>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("games")
            .get()
            .addOnSuccessListener { result ->
                val games = result.map { document -> document.toObject(Gamemodel::class.java) }
                onSuccess(games)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun addUserToFirestore(user: Usermodel, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users")
            .add(user)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getUsersFromFirestore(onSuccess: (List<Usermodel>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val users = result.map { document -> document.toObject(Usermodel::class.java) }
                onSuccess(users)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun addReviewToFirestore(review: ReviewModel, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("reviews")
            .add(review)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getReviewsFromFirestore(onSuccess: (List<ReviewModel>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("reviews")
            .get()
            .addOnSuccessListener { result ->
                val reviews = result.map { document -> document.toObject(ReviewModel::class.java) }
                onSuccess(reviews)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    suspend fun addGameToFirestoreSuspend(game: Gamemodel) {
        db.collection("games").add(game).await()
    }

    suspend fun getGamesFromFirestoreSuspend(): List<Gamemodel> {
        val result = db.collection("games").get().await()
        return result.documents.map { it.toObject(Gamemodel::class.java)!! }
    }

    suspend fun addUserToFirestoreSuspend(user: Usermodel) {
        db.collection("users").add(user).await()
    }

    suspend fun getUsersFromFirestoreSuspend(): List<Usermodel> {
        val result = db.collection("users").get().await()
        return result.documents.map { it.toObject(Usermodel::class.java)!! }
    }

    suspend fun signInUser(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            auth.currentUser != null
        } catch (e: Exception) {
            false
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun logoutUser() {
        auth.signOut()
    }

    suspend fun checkUserExists(email: String): Boolean {
        val result = db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()

        return result.documents.isNotEmpty()
    }
}
