package com.example.gamevault.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamevault.SQLite.DBhelper
import com.example.gamevault.model.Gamemodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val dbHelper: DBhelper) : ViewModel() {
    private val _games = MutableLiveData<List<Gamemodel>>()
    val games: LiveData<List<Gamemodel>> get() = _games

    init {
        loadGames()
    }

    private fun loadGames() {
        viewModelScope.launch {
            try {
                // Perform database operation on IO thread
                val gamesList = withContext(Dispatchers.IO) { dbHelper.getAllGames() }
                Log.d("HomeViewModel", "Games loaded: ${gamesList.size}")
                // Post results back to the main thread
                _games.value = gamesList
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading games", e)
            }
        }
    }
}
