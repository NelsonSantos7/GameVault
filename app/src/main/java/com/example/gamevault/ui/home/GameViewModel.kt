package com.example.gamevault.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gamevault.model.Gamemodel
import kotlinx.coroutines.launch

class GameViewModel(private val repository: GameRepository) : ViewModel() {
    private val _games = MutableLiveData<List<Gamemodel>>()
    val games: LiveData<List<Gamemodel>> = _games
    private val _status = MutableLiveData<LoadStatus>()
    val status: LiveData<LoadStatus> = _status

    init {
        loadGames()
    }

    private fun loadGames() {
        viewModelScope.launch {
            _status.value = LoadStatus.Loading
            try {
                _games.value = repository.getGames()
                _status.value = LoadStatus.Success
            } catch (e: Exception) {
                _status.value = LoadStatus.Error
            }
        }
    }

    class GameViewModelFactory(private val repository: GameRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GameViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


    enum class LoadStatus {
        Loading, Success, Error
    }
}
