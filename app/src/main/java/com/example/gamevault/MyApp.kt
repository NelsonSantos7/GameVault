package com.example.gamevault

import android.app.Application
import com.example.gamevault.SQLite.DBhelper
import com.example.gamevault.ui.home.GameRepository

class MyApp : Application() {
    lateinit var repository: GameRepository
    private lateinit var dbHelper: DBhelper

    override fun onCreate() {
        super.onCreate()
        dbHelper = DBhelper(this)
        repository = GameRepository(dbHelper)
    }
}
