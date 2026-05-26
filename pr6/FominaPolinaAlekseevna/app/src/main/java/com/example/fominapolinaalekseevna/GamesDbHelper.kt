package com.example.fominapolinaalekseevna

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GamesDbHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE ${GameContract.Game.TABLE} (
                ${GameContract.Game._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${GameContract.Game.TITLE} TEXT NOT NULL,
                ${GameContract.Game.CATEGORY} TEXT NOT NULL,
                ${GameContract.Game.PLAYERS} INTEGER NOT NULL,
                ${GameContract.Game.RATING} REAL NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${GameContract.Game.TABLE}")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "games.db"
        private const val DATABASE_VERSION = 1
    }
}
