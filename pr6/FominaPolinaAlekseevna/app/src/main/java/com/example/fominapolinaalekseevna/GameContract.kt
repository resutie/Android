package com.example.fominapolinaalekseevna

import android.net.Uri

object GameContract {
    const val AUTHORITY = "com.example.fominapolinaalekseevna.provider"
    val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/games")

    object Game {
        const val TABLE = "games"
        const val _ID = "_id"
        const val TITLE = "title"
        const val CATEGORY = "category"
        const val PLAYERS = "players"
        const val RATING = "rating"
    }
}
