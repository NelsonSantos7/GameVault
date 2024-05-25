package com.example.gamevault

import android.app.Application
import com.example.gamevault.firebase.FirebaseHelper
import com.example.gamevault.ui.home.GameRepository
import com.google.firebase.FirebaseApp

class MyApp : Application() {
    lateinit var repository: GameRepository
        private set

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        val firebaseHelper = FirebaseHelper()
        repository = GameRepository(firebaseHelper)
    }
}
