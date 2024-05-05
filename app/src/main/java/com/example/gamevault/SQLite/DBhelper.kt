package com.example.gamevault.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gamevault.model.Gamemodel
import com.example.gamevault.model.Usermodel
import org.mindrot.jbcrypt.BCrypt

class DBhelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "GameVaultDB.db"

        // Tables
        const val TABLE_USERS = "Usuarios"
        const val TABLE_GAMES = "Jogos"

        // Columns for table Usuarios
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"

        // Columns for table Jogos
        const val COLUMN_GAME_ID = "game_id"
        const val COLUMN_GAME_TITLE = "titulo"
        const val COLUMN_GAME_DISTRIBUTOR = "distribuidora"
        const val COLUMN_GAME_YEAR = "ano_lancamento"
        const val COLUMN_GAME_PHOTO = "foto"
        const val COLUMN_GAME_TRAILER = "mini_trailer"
        const val COLUMN_GAME_SUMMARY = "resumo"
        const val COLUMN_GAME_DURATION = "tempo_estimado"
        const val COLUMN_GAME_PLATFORMS = "plataformas"
        const val COLUMN_GAME_STATUS = "game_status"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_USERS (" +
                    "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_USERNAME TEXT UNIQUE," +
                    "$COLUMN_EMAIL TEXT UNIQUE," +
                    "$COLUMN_PASSWORD TEXT)"
        )

        db.execSQL(
            "CREATE TABLE $TABLE_GAMES (" +
                    "$COLUMN_GAME_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_GAME_TITLE TEXT," +
                    "$COLUMN_GAME_DISTRIBUTOR TEXT," +
                    "$COLUMN_GAME_YEAR INTEGER," +
                    "$COLUMN_GAME_PHOTO TEXT," +
                    "$COLUMN_GAME_TRAILER TEXT," +
                    "$COLUMN_GAME_SUMMARY TEXT," +
                    "$COLUMN_GAME_DURATION INTEGER," +
                    "$COLUMN_GAME_PLATFORMS TEXT," +
                    "$COLUMN_GAME_STATUS INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
        onCreate(db)
    }

    fun addUser(user: Usermodel): Long {
        return writableDatabase.use { db ->
            ContentValues().apply {
                put(COLUMN_USERNAME, user.username)
                put(COLUMN_EMAIL, user.email)
                put(COLUMN_PASSWORD, BCrypt.hashpw(user.password, BCrypt.gensalt()))
            }.let { values ->
                db.insert(TABLE_USERS, null, values)
            }
        }
    }

    fun getUserByUsername(username: String): Usermodel? {
        return readableDatabase.use { db ->
            db.query(
                TABLE_USERS,
                null,
                "$COLUMN_USERNAME = ?",
                arrayOf(username),
                null,
                null,
                null
            ).use { cursor ->
                if (cursor.moveToFirst()) {
                    Usermodel(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                        username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                        email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                        password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
                    )
                } else null
            }
        }
    }

    fun checkUserExists(email: String, username: String): Boolean {
        return readableDatabase.use { db ->
            val columns = arrayOf(COLUMN_USER_ID)
            val selection = "$COLUMN_EMAIL = ? OR $COLUMN_USERNAME = ?"
            val selectionArgs = arrayOf(email, username)
            db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null).use { cursor ->
                cursor.moveToFirst() && cursor.count > 0
            }
        }
    }

    fun checkPasswordHash(username: String, plainPassword: String): Boolean {
        return readableDatabase.use { db ->
            val cursor = db.query(
                TABLE_USERS,
                arrayOf(COLUMN_PASSWORD),
                "$COLUMN_USERNAME = ?",
                arrayOf(username),
                null, null, null
            )
            if (cursor.moveToFirst()) {
                val storedPasswordHash = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
                cursor.close()
                return BCrypt.checkpw(plainPassword, storedPasswordHash)
            }
            cursor.close()
            return false
        }
    }

    fun addGame(game: Gamemodel): Long {
        return writableDatabase.use { db ->
            db.beginTransaction()
            try {
                val values = ContentValues().apply {
                    put(COLUMN_GAME_TITLE, game.titulo)
                    put(COLUMN_GAME_DISTRIBUTOR, game.distribuidora)
                    put(COLUMN_GAME_YEAR, game.anoLancamento)
                    put(COLUMN_GAME_PHOTO, game.fotoUrl)
                    put(COLUMN_GAME_TRAILER, game.miniTrailer)
                    put(COLUMN_GAME_SUMMARY, game.resumo)
                    put(COLUMN_GAME_DURATION, game.tempoEstimado)
                    put(COLUMN_GAME_PLATFORMS, game.plataformas)
                    put(COLUMN_GAME_STATUS, game.status)
                }
                val result = db.insert(TABLE_GAMES, null, values)
                db.setTransactionSuccessful()
                result
            } finally {
                db.endTransaction()
            }
        }
    }

    fun getAllGames(): List<Gamemodel> {
        return readableDatabase.use { db ->
            db.query(
                TABLE_GAMES,
                null,
                null,
                null,
                null,
                null,
                "$COLUMN_GAME_TITLE ASC"
            ).use { cursor ->
                generateSequence { if (cursor.moveToNext()) cursor else null }
                    .map { Gamemodel.fromCursor(it) }
                    .toList()
            }
        }
    }

    fun getAllGamesLiveData(): LiveData<List<Gamemodel>> {
        val gamesLiveData = MutableLiveData<List<Gamemodel>>()
        Thread {
            val gamesList = getAllGames()
            gamesLiveData.postValue(gamesList)
        }.start()
        return gamesLiveData
    }

    fun getGameById(gameId: Int): Gamemodel? {
        return readableDatabase.use { db ->
            db.query(
                TABLE_GAMES,
                null,
                "$COLUMN_GAME_ID = ?",
                arrayOf(gameId.toString()),
                null, null, null
            ).use { cursor ->
                if (cursor.moveToFirst()) Gamemodel.fromCursor(cursor) else null
            }
        }
    }
}
