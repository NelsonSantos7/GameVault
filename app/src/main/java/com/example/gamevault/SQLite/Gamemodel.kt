package com.example.gamevault.model

import android.database.Cursor
import com.example.gamevault.SQLite.DBhelper

data class Gamemodel(
    val id: Int? = null,
    val titulo: String,
    val distribuidora: String,
    val anoLancamento: Int,
    val fotoUrl: String,
    val plataformas: String,
    val miniTrailer: String,
    val resumo: String,
    val tempoEstimado: Int,
    val status: Int
) {
    companion object {
        fun fromCursor(cursor: Cursor): Gamemodel {
            if (cursor.isNull(cursor.getColumnIndexOrThrow(DBhelper.COLUMN_GAME_ID))) {
                throw IllegalArgumentException("Cursor was expected to contain id but was null")
            }

            return Gamemodel(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DBhelper.COLUMN_GAME_ID)),
                titulo = cursor.getString(cursor.getColumnIndexOrThrow(DBhelper.COLUMN_GAME_TITLE)),
                distribuidora = cursor.getString(cursor.getColumnIndexOrThrow(DBhelper.COLUMN_GAME_DISTRIBUTOR)),
                anoLancamento = cursor.getInt(cursor.getColumnIndexOrThrow(DBhelper.COLUMN_GAME_YEAR)),
                fotoUrl = cursor.getString(cursor.getColumnIndexOrThrow(DBhelper.COLUMN_GAME_PHOTO)),
                plataformas = cursor.getString(cursor.getColumnIndexOrThrow(DBhelper.COLUMN_GAME_PLATFORMS)),
                miniTrailer = cursor.getString(cursor.getColumnIndexOrThrow(DBhelper.COLUMN_GAME_TRAILER)),
                resumo = cursor.getString(cursor.getColumnIndexOrThrow(DBhelper.COLUMN_GAME_SUMMARY)),
                tempoEstimado = cursor.getInt(cursor.getColumnIndexOrThrow(DBhelper.COLUMN_GAME_DURATION)),
                status = cursor.getInt(cursor.getColumnIndexOrThrow(DBhelper.COLUMN_GAME_STATUS))
            )
        }
    }
}
