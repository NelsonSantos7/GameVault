package com.example.gamevault.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBhelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "GameVaultDB"

        // Tabela de Usuários
        private const val TABLE_USERS = "Usuarios"
        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"

        // Tabela de Jogos
        private const val TABLE_GAMES = "Jogos"
        private const val COLUMN_GAME_ID = "game_id"
        private const val COLUMN_GAME_TITLE = "titulo"
        private const val COLUMN_GAME_DISTRIBUTOR = "distribuidora"
        private const val COLUMN_GAME_METACRITIC = "nota_metacritic"
        private const val COLUMN_GAME_YEAR = "ano_lancamento"
        private const val COLUMN_GAME_PHOTO = "foto"
        private const val COLUMN_GAME_TRAILER = "mini_trailer"
        private const val COLUMN_GAME_SUMMARY = "resumo"
        private const val COLUMN_GAME_DURATION = "tempo_estimado"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_USERS_TABLE = ("CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_USERNAME TEXT UNIQUE," +
                "$COLUMN_PASSWORD TEXT)")

        val CREATE_GAMES_TABLE = ("CREATE TABLE $TABLE_GAMES (" +
                "$COLUMN_GAME_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_GAME_TITLE TEXT," +
                "$COLUMN_GAME_DISTRIBUTOR TEXT," +
                "$COLUMN_GAME_METACRITIC REAL," +
                "$COLUMN_GAME_YEAR INTEGER," +
                "$COLUMN_GAME_PHOTO BLOB," +
                "$COLUMN_GAME_TRAILER TEXT," +
                "$COLUMN_GAME_SUMMARY TEXT," +
                "$COLUMN_GAME_DURATION INTEGER)")

        db.execSQL(CREATE_USERS_TABLE)
        db.execSQL(CREATE_GAMES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
        onCreate(db)
    }

    // Métodos para gerenciamento de usuários
    fun addUser(user: Usermodel): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, user.username)
            put(COLUMN_PASSWORD, user.password)
        }
        val id = db.insert(TABLE_USERS, null, values)
        db.close()
        return id
    }

    fun updateUser(user: Usermodel): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, user.username)
            put(COLUMN_PASSWORD, user.password)
        }
        val result = db.update(TABLE_USERS, values, "$COLUMN_USER_ID = ?", arrayOf(user.id.toString()))
        db.close()
        return result
    }

    fun deleteUser(userId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_USERS, "$COLUMN_USER_ID = ?", arrayOf(userId.toString()))
        db.close()
        return result
    }

    fun validateLogin(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_USER_ID),
            "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?", arrayOf(username, password),
            null, null, null)
        val isLoggedIn = cursor.moveToFirst()
        cursor.close()
        db.close()
        return isLoggedIn
    }

    // Métodos de Gerenciamento de Jogos
    fun addGame(game: Gamemodel): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_GAME_TITLE, game.titulo)
            put(COLUMN_GAME_DISTRIBUTOR, game.distribuidora)
            put(COLUMN_GAME_METACRITIC, game.notaMetacritic)
            put(COLUMN_GAME_YEAR, game.anoLancamento)
            put(COLUMN_GAME_PHOTO, game.foto)
            put(COLUMN_GAME_TRAILER, game.miniTrailer)
            put(COLUMN_GAME_SUMMARY, game.resumo)
            put(COLUMN_GAME_DURATION, game.tempoEstimado)
        }
        val _id = db.insert(TABLE_GAMES, null, values)
        db.close()
        return _id
    }

    fun updateGame(game: Gamemodel): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_GAME_TITLE, game.titulo)
            put(COLUMN_GAME_DISTRIBUTOR, game.distribuidora)
            put(COLUMN_GAME_METACRITIC, game.notaMetacritic)
            put(COLUMN_GAME_YEAR, game.anoLancamento)
            put(COLUMN_GAME_PHOTO, game.foto)
            put(COLUMN_GAME_TRAILER, game.miniTrailer)
            put(COLUMN_GAME_SUMMARY, game.resumo)
            put(COLUMN_GAME_DURATION, game.tempoEstimado)
        }
        val result = db.update(TABLE_GAMES, values, "$COLUMN_GAME_ID = ?", arrayOf(game.id.toString()))
        db.close()
        return result
    }

    fun deleteGame(gameId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_GAMES, "$COLUMN_GAME_ID = ?", arrayOf(gameId.toString()))
        db.close()
        return result
    }

    fun getAllGames(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_GAMES", null)
    }

    fun getGameById(gameId: Int): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_GAMES WHERE $COLUMN_GAME_ID = ?", arrayOf(gameId.toString()))
    }
}
