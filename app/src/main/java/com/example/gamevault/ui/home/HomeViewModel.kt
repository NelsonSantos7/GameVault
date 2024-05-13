package com.example.gamevault.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamevault.model.Gamemodel
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: GameRepository) : ViewModel() {
    private val _games = MutableLiveData<List<Gamemodel>>()
    val games: LiveData<List<Gamemodel>> get() = _games
    private val _status = MutableLiveData<LoadStatus>()
    val status: LiveData<LoadStatus> get() = _status

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

    enum class LoadStatus {
        Loading, Success, Error
    }
}
