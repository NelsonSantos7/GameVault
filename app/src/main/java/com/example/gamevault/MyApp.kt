package com.example.gamevault

import android.app.Application
import com.example.gamevault.firebase.FirebaseHelper
import com.example.gamevault.ui.home.GameRepository
import com.google.firebase.FirebaseApp

class MyApp : Application() {
    lateinit var firebaseHelper: FirebaseHelper
    lateinit var repository: GameRepository

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        firebaseHelper = FirebaseHelper()
        repository = GameRepository(firebaseHelper)
    }
}
