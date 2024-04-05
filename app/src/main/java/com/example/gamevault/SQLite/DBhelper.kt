package com.example.gamevault.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBhelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "GameVaultDB"
        const val TABLE_USERS = "Usuarios"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
        const val TABLE_GAMES = "Jogos"
        const val COLUMN_GAME_ID = "game_id"
        const val COLUMN_GAME_TITLE = "titulo"
        const val COLUMN_GAME_DISTRIBUTOR = "distribuidora"
        const val COLUMN_GAME_METACRITIC = "nota_metacritic"
        const val COLUMN_GAME_YEAR = "ano_lancamento"
        const val COLUMN_GAME_PHOTO = "foto"
        const val COLUMN_GAME_TRAILER = "mini_trailer"
        const val COLUMN_GAME_SUMMARY = "resumo"
        const val COLUMN_GAME_DURATION = "tempo_estimado"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_USERS_TABLE = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT UNIQUE,
                $COLUMN_EMAIL TEXT UNIQUE,
                $COLUMN_PASSWORD TEXT
            )
        """.trimIndent()

        val CREATE_GAMES_TABLE = """
            CREATE TABLE $TABLE_GAMES (
                $COLUMN_GAME_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_GAME_TITLE TEXT,
                $COLUMN_GAME_DISTRIBUTOR TEXT,
                $COLUMN_GAME_METACRITIC REAL,
                $COLUMN_GAME_YEAR INTEGER,
                $COLUMN_GAME_PHOTO BLOB,
                $COLUMN_GAME_TRAILER TEXT,
                $COLUMN_GAME_SUMMARY TEXT,
                $COLUMN_GAME_DURATION INTEGER
            )
        """.trimIndent()

        db.execSQL(CREATE_USERS_TABLE)
        db.execSQL(CREATE_GAMES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
        onCreate(db)
    }

    fun addUser(user: Usermodel): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, user.username)
            put(COLUMN_EMAIL, user.email)
            put(COLUMN_PASSWORD, user.password)
        }
        val id = db.insert(TABLE_USERS, null, values)
        db.close()
        return id
    }
    fun getUserByUsername(username: String): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?", arrayOf(username))
    }


    fun getUserById(userId: Int): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_USER_ID = ?", arrayOf(userId.toString()))
    }

    fun updateUserProfile(userId: Int, newUserName: String, newUserEmail: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, newUserName)
            put(COLUMN_EMAIL, newUserEmail)
        }
        val success = db.update(TABLE_USERS, values, "$COLUMN_USER_ID = ?", arrayOf(userId.toString()))
        db.close()
        return success
    }

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
        val id = db.insert(TABLE_GAMES, null, values)
        db.close()
        return id
    }

    fun updateGame(gameId: Int, game: Gamemodel): Int {
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
        val success = db.update(TABLE_GAMES, values, "$COLUMN_GAME_ID = ?", arrayOf(gameId.toString()))
        db.close()
        return success
    }

    fun deleteGame(gameId: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_GAMES, "$COLUMN_GAME_ID = ?", arrayOf(gameId.toString()))
        db.close()
        return success
    }

    fun getAllGames(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_GAMES", null)
    }

    fun getGameById(gameId: Int): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_GAMES WHERE $COLUMN_GAME_ID = ?", arrayOf(gameId.toString()))
    }


    fun checkUserExists(email: String, username: String): Boolean {
        val db = readableDatabase
        val emailQuery = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?"
        val usernameQuery = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"

        // Verifica se o e-mail já existe
        readableDatabase.rawQuery(emailQuery, arrayOf(email)).use { cursor ->
            if (cursor.moveToFirst()) {
                return true // E-mail encontrado, usuário já existe
            }
        }

        // Verifica se o nome de usuário já existe
        readableDatabase.rawQuery(usernameQuery, arrayOf(username)).use { cursor ->
            if (cursor.moveToFirst()) {
                return true // Nome de usuário encontrado, usuário já existe
            }
        }

        return false // Usuário não existe
    }



}
