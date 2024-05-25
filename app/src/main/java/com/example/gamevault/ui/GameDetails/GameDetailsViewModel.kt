package com.example.gamevault.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamevault.firebase.FirebaseHelper
import kotlinx.coroutines.launch

class GameDetailsViewModel(private val firebaseHelper: FirebaseHelper) : ViewModel() {

    fun updateGameStatus(gameId: String, status: Int) {
        viewModelScope.launch {
            try {
                firebaseHelper.updateGameStatus(gameId, status)
                // Sucesso na atualização do status do jogo
            } catch (e: Exception) {
                // Tratar o erro ao atualizar o status do jogo
            }
        }
    }
}
