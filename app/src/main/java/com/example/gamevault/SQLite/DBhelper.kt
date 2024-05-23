package com.example.gamevault.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.gamevault.model.Gamemodel
import com.example.gamevault.model.ReviewModel

class DBhelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "GameVaultDB.db"

        const val TABLE_GAMES = "Jogos"
        const val TABLE_REVIEWS = "reviews"
        const val TABLE_USERS = "users"

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
        const val COLUMN_USER_ID = "user_id"

        const val COLUMN_REVIEW_ID = "review_id"
        const val COLUMN_REVIEW_GAME_ID = "game_id"
        const val COLUMN_REVIEW_USER_ID = "user_id"
        const val COLUMN_REVIEW_RATING = "rating"
        const val COLUMN_REVIEW_COMMENT = "comment"

        const val COLUMN_USER_EMAIL = "email"
        const val COLUMN_USER_PASSWORD = "password"
        const val COLUMN_USER_USERNAME = "username"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_GAMES (
                $COLUMN_GAME_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_GAME_TITLE TEXT,
                $COLUMN_GAME_DISTRIBUTOR TEXT,
                $COLUMN_GAME_YEAR INTEGER,
                $COLUMN_GAME_PHOTO TEXT,
                $COLUMN_GAME_TRAILER TEXT,
                $COLUMN_GAME_SUMMARY TEXT,
                $COLUMN_GAME_DURATION INTEGER,
                $COLUMN_GAME_PLATFORMS TEXT,
                $COLUMN_GAME_STATUS INTEGER,
                $COLUMN_USER_ID INTEGER,
                FOREIGN KEY($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
            """
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_EMAIL TEXT,
                $COLUMN_USER_PASSWORD TEXT,
                $COLUMN_USER_USERNAME TEXT
            )
            """
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_REVIEWS (
                $COLUMN_REVIEW_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_REVIEW_GAME_ID INTEGER,
                $COLUMN_REVIEW_USER_ID INTEGER,
                $COLUMN_REVIEW_RATING INTEGER,
                $COLUMN_REVIEW_COMMENT TEXT,
                FOREIGN KEY($COLUMN_REVIEW_GAME_ID) REFERENCES $TABLE_GAMES($COLUMN_GAME_ID),
                FOREIGN KEY($COLUMN_REVIEW_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
            """
        )

        db.execSQL("CREATE INDEX idx_game_title ON $TABLE_GAMES($COLUMN_GAME_TITLE)")
        db.execSQL("CREATE INDEX idx_distributor ON $TABLE_GAMES($COLUMN_GAME_DISTRIBUTOR)")
        db.execSQL("CREATE INDEX idx_review_game_id ON $TABLE_REVIEWS($COLUMN_REVIEW_GAME_ID)")
        db.execSQL("CREATE INDEX idx_review_user_id ON $TABLE_REVIEWS($COLUMN_REVIEW_USER_ID)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 3) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_REVIEWS")
            onCreate(db)
        }
    }

    fun addGame(game: Gamemodel, userId: Int): Long {
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
            put(COLUMN_USER_ID, userId)
        }
        return writableDatabase.insert(TABLE_GAMES, null, values)
    }

    fun getGamesByStatusAndUser(status: Int, userId: Int): List<Gamemodel> {
        return readableDatabase.query(
            TABLE_GAMES,
            null,
            "$COLUMN_GAME_STATUS = ? AND $COLUMN_USER_ID = ?",
            arrayOf(status.toString(), userId.toString()),
            null,
            null,
            "$COLUMN_GAME_TITLE ASC"
        ).use { cursor ->
            generateSequence { if (cursor.moveToNext()) cursor else null }
                .map { Gamemodel.fromCursor(it) }
                .toList()
        }
    }

    fun getAllGames(): List<Gamemodel> {
        return readableDatabase.query(
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

    fun addReview(review: ReviewModel): Long {
        val values = ContentValues().apply {
            put(COLUMN_REVIEW_GAME_ID, review.gameId)
            put(COLUMN_REVIEW_USER_ID, review.userId)
            put(COLUMN_REVIEW_RATING, review.rating)
            put(COLUMN_REVIEW_COMMENT, review.comment)
        }
        return writableDatabase.insert(TABLE_REVIEWS, null, values)
    }

    fun getReviewsByGame(gameId: Int): List<ReviewModel> {
        return readableDatabase.query(
            TABLE_REVIEWS,
            null,
            "$COLUMN_REVIEW_GAME_ID = ?",
            arrayOf(gameId.toString()),
            null,
            null,
            "$COLUMN_REVIEW_ID ASC"
        ).use { cursor ->
            generateSequence { if (cursor.moveToNext()) cursor else null }
                .map {
                    ReviewModel(
                        id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_ID)),
                        gameId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_GAME_ID)),
                        userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_USER_ID)),
                        rating = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_RATING)),
                        comment = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_COMMENT))
                    )
                }
                .toList()
        }
    }
}
