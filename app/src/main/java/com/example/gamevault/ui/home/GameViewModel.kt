package com.example.gamevault.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.gamevault.model.Gamemodel
import com.example.gamevault.ui.home.GameRepository
import kotlinx.coroutines.Dispatchers

class GameViewModel(private val repository: GameRepository) : ViewModel() {

    val games: LiveData<List<Gamemodel>> = liveData(Dispatchers.IO) {
        val retrievedGames = repository.getAllGames()
        emit(retrievedGames)
    }
}
