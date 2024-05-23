package com.example.gamevault.model

object GameStatus {
    const val EM_PROGRESSO = 0
    const val NA_LISTA = 1
    const val FINALIZADOS = 2

    fun fromInt(value: Int): Int? {
        return when (value) {
            EM_PROGRESSO -> EM_PROGRESSO
            NA_LISTA -> NA_LISTA
            FINALIZADOS -> FINALIZADOS
            else -> null
        }
    }
}
