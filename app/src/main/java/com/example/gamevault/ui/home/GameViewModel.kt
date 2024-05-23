package com.example.gamevault.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

class GameViewModel(private val repository: GameRepository) : ViewModel() {

    val games = liveData(Dispatchers.IO) {
        val retrievedGames = repository.getAllGames()
        emit(retrievedGames)
    }

    class Factory(private val repository: GameRepository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GameViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
