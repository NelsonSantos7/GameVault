package com.example.gamevault.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gamevault.firebase.FirebaseHelper

class GameDetailsViewModelFactory(
    private val firebaseHelper: FirebaseHelper
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameDetailsViewModel::class.java)) {
            return GameDetailsViewModel(firebaseHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
