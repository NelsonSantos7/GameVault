package com.example.gamevault.ui.home

import com.example.gamevault.SQLite.DBhelper
import com.example.gamevault.model.Gamemodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRepository(private val dbHelper: DBhelper) {
    suspend fun getGames(): List<Gamemodel> = withContext(Dispatchers.IO) {
        dbHelper.getAllGames()
    }

    suspend fun insertGame(game: Gamemodel): Long = withContext(Dispatchers.IO) {
        dbHelper.addGame(game)
    }

    suspend fun updateGame(game: Gamemodel): Int = withContext(Dispatchers.IO) {
        dbHelper.updateGame(game)
    }

    suspend fun deleteGame(game: Gamemodel): Int = withContext(Dispatchers.IO) {
        dbHelper.deleteGame(game)
    }
}
