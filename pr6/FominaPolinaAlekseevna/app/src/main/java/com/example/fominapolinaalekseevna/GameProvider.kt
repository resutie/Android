package com.example.fominapolinaalekseevna

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

class GameProvider : ContentProvider() {

    private lateinit var db: SQLiteDatabase

    override fun onCreate(): Boolean {
        val helper = GamesDbHelper(requireContext())
        db = helper.writableDatabase
        seedIfEmpty()
        return true
    }

    private fun seedIfEmpty() {
        val cursor = db.query(
            GameContract.Game.TABLE,
            arrayOf(GameContract.Game._ID),
            null,
            null,
            null,
            null,
            null,
            "1"
        )
        val hasData = cursor.use { it.moveToFirst() }
        if (hasData) return

        val samples = listOf(
            contentValues("Catan", "Стратегии", 4, 4.8f),
            contentValues("Dixit", "Семейные", 6, 4.5f),
            contentValues("Пандемия", "Кооперативные", 4, 4.7f)
        )
        samples.forEach { db.insert(GameContract.Game.TABLE, null, it) }
    }

    private fun contentValues(
        title: String,
        category: String,
        players: Int,
        rating: Float
    ): ContentValues = ContentValues().apply {
        put(GameContract.Game.TITLE, title)
        put(GameContract.Game.CATEGORY, category)
        put(GameContract.Game.PLAYERS, players)
        put(GameContract.Game.RATING, rating)
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            GAMES -> db.query(
                GameContract.Game.TABLE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder ?: "${GameContract.Game.TITLE} ASC"
            )

            GAME_ID -> {
                val id = ContentUris.parseId(uri)
                db.query(
                    GameContract.Game.TABLE,
                    projection,
                    "${GameContract.Game._ID}=?",
                    arrayOf(id.toString()),
                    null,
                    null,
                    sortOrder
                )
            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String = when (uriMatcher.match(uri)) {
        GAMES -> "vnd.android.cursor.dir/vnd.${GameContract.AUTHORITY}.games"
        GAME_ID -> "vnd.android.cursor.item/vnd.${GameContract.AUTHORITY}.games"
        else -> throw IllegalArgumentException("Unknown URI: $uri")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (values == null) return null
        val rowId = db.insert(GameContract.Game.TABLE, null, values)
        if (rowId == -1L) return null
        val newUri = ContentUris.withAppendedId(GameContract.CONTENT_URI, rowId)
        requireContext().contentResolver.notifyChange(newUri, null)
        return newUri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val count = when (uriMatcher.match(uri)) {
            GAMES -> db.delete(GameContract.Game.TABLE, selection, selectionArgs)
            GAME_ID -> {
                val id = ContentUris.parseId(uri)
                db.delete(
                    GameContract.Game.TABLE,
                    "${GameContract.Game._ID}=?",
                    arrayOf(id.toString())
                )
            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        requireContext().contentResolver.notifyChange(uri, null)
        return count
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val count = when (uriMatcher.match(uri)) {
            GAMES -> db.update(GameContract.Game.TABLE, values, selection, selectionArgs)
            GAME_ID -> {
                val id = ContentUris.parseId(uri)
                db.update(
                    GameContract.Game.TABLE,
                    values,
                    "${GameContract.Game._ID}=?",
                    arrayOf(id.toString())
                )
            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        requireContext().contentResolver.notifyChange(uri, null)
        return count
    }

    companion object {
        private const val GAMES = 1
        private const val GAME_ID = 2

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(GameContract.AUTHORITY, "games", GAMES)
            addURI(GameContract.AUTHORITY, "games/#", GAME_ID)
        }
    }
}
