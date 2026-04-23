package com.example.fominapolinaalekseevna

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "library.db", null, 1) {

    companion object {
        const val TABLE = "books"
        const val COL_ID = "id"
        const val COL_TITLE = "title"
        const val COL_AUTHOR = "author"
        const val COL_YEAR = "year"
        const val COL_GENRE = "genre"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TITLE TEXT,
                $COL_AUTHOR TEXT,
                $COL_YEAR INTEGER,
                $COL_GENRE TEXT
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    fun addBook(book: Book): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TITLE, book.title)
            put(COL_AUTHOR, book.author)
            put(COL_YEAR, book.year)
            put(COL_GENRE, book.genre)
        }
        val inserted = db.insert(TABLE, null, values)
        db.close()
        return inserted != -1L
    }

    fun findBook(title: String): Book? {
        val db = readableDatabase
        val cursor = db.query(TABLE, null, "$COL_TITLE = ?", arrayOf(title), null, null, null)
        val book = if (cursor.moveToFirst()) {
            Book(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
                author = cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTHOR)),
                year = cursor.getInt(cursor.getColumnIndexOrThrow(COL_YEAR)),
                genre = cursor.getString(cursor.getColumnIndexOrThrow(COL_GENRE))
            )
        } else {
            null
        }
        cursor.close()
        db.close()
        return book
    }

    fun getAllBooks(): List<Book> {
        val books = mutableListOf<Book>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE", null)
        if (cursor.moveToFirst()) {
            do {
                books.add(
                    Book(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
                        author = cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTHOR)),
                        year = cursor.getInt(cursor.getColumnIndexOrThrow(COL_YEAR)),
                        genre = cursor.getString(cursor.getColumnIndexOrThrow(COL_GENRE))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return books
    }

    fun updateBook(oldTitle: String, newBook: Book): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TITLE, newBook.title)
            put(COL_AUTHOR, newBook.author)
            put(COL_YEAR, newBook.year)
            put(COL_GENRE, newBook.genre)
        }
        val updatedRows = db.update(TABLE, values, "$COL_TITLE = ?", arrayOf(oldTitle))
        db.close()
        return updatedRows > 0
    }

    fun deleteBook(title: String): Boolean {
        val db = writableDatabase
        val deletedRows = db.delete(TABLE, "$COL_TITLE = ?", arrayOf(title))
        db.close()
        return deletedRows > 0
    }
}
