package com.example.gamevault.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.gamevault.model.Gamemodel
import kotlinx.coroutines.Dispatchers

class HomeViewModel(private val repository: GameRepository) : ViewModel() {

    val games: LiveData<List<Gamemodel>> = liveData(Dispatchers.IO) {
        val retrievedGames = repository.getAllGames()
        emit(retrievedGames)
    }

    fun updateGameStatus(gameId: String, status: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        repository.updateGameStatus(gameId, status, onSuccess, onError)
    }

    class Factory(private val repository: GameRepository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
