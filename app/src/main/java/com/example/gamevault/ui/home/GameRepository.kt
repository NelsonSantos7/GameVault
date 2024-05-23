package com.example.gamevault.ui.home

import com.example.gamevault.firebase.FirebaseHelper
import com.example.gamevault.model.Gamemodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRepository(private val firebaseHelper: FirebaseHelper) {

    suspend fun getAllGames(): List<Gamemodel> = withContext(Dispatchers.IO) {
        firebaseHelper.getGamesFromFirestoreSuspend()
    }
}
