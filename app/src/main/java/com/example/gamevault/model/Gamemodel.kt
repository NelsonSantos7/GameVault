package com.example.gamevault.model

import android.database.Cursor
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gamemodel(
    val id: Long? = null,
    val titulo: String = "",
    val distribuidora: String = "",
    val anoLancamento: Int = 0,
    val fotoUrl: String = "",
    val miniTrailer: String = "",
    val resumo: String = "",
    val tempoEstimado: Int = 0,
    val plataformas: String = "",
    val status: Int = 0,
    val userId: Int = 0
) : Parcelable {
    companion object {
        fun fromCursor(cursor: Cursor): Gamemodel {
            return Gamemodel(
                id = cursor.getLong(cursor.getColumnIndexOrThrow("game_id")),
                titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                distribuidora = cursor.getString(cursor.getColumnIndexOrThrow("distribuidora")),
                anoLancamento = cursor.getInt(cursor.getColumnIndexOrThrow("ano_lancamento")),
                fotoUrl = cursor.getString(cursor.getColumnIndexOrThrow("foto")),
                miniTrailer = cursor.getString(cursor.getColumnIndexOrThrow("mini_trailer")),
                resumo = cursor.getString(cursor.getColumnIndexOrThrow("resumo")),
                tempoEstimado = cursor.getInt(cursor.getColumnIndexOrThrow("tempo_estimado")),
                plataformas = cursor.getString(cursor.getColumnIndexOrThrow("plataformas")),
                status = cursor.getInt(cursor.getColumnIndexOrThrow("game_status")),
                userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"))
            )
        }
    }
}
