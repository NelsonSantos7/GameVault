package com.example.gamevault.ui.home

import com.example.gamevault.firebase.FirebaseHelper
import com.example.gamevault.model.Gamemodel

class GameRepository(private val firebaseHelper: FirebaseHelper) {

    suspend fun getAllGames(): List<Gamemodel> {
        return firebaseHelper.getGamesFromFirestore()
    }

    suspend fun getGamesByStatusAndUser(status: Int, userId: String): List<Gamemodel> {
        return firebaseHelper.getGamesByStatusAndUser(status, userId)
    }

    fun updateGameStatus(gameId: String, status: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        firebaseHelper.updateGameStatus(gameId, status)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Unknown error") }
    }
}
