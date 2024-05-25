package com.example.gamevault.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamevault.firebase.FirebaseHelper
import com.example.gamevault.model.NoticiasModel
import kotlinx.coroutines.launch

class NoticiasViewModel : ViewModel() {
    private val firebaseHelper = FirebaseHelper()
    val noticias = MutableLiveData<List<NoticiasModel>>()
    val errorMessage = MutableLiveData<String>()

    fun fetchNoticias() {
        viewModelScope.launch {
            try {
                val newsList = firebaseHelper.getNoticiasFromFirestore()
                noticias.postValue(newsList)
            } catch (e: Exception) {
                errorMessage.postValue("Erro ao buscar notícias: ${e.message}")
            }
        }
    }
}
