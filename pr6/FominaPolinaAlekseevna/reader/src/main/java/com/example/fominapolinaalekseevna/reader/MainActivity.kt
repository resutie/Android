package com.example.fominapolinaalekseevna.reader

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var output: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        output = findViewById(R.id.tvReaderOutput)
        findViewById<android.widget.Button>(R.id.btnLoadProvider).setOnClickListener {
            loadGamesFromProvider()
        }
        loadGamesFromProvider()
    }

    private fun loadGamesFromProvider() {
        output.text = getString(R.string.reader_loading)
        val uri = Uri.parse(PROVIDER_URI)
        val cursor: Cursor? = try {
            contentResolver.query(
                uri,
                arrayOf("_id", "title", "category", "players", "rating"),
                null,
                null,
                "title ASC"
            )
        } catch (e: SecurityException) {
            showError(getString(R.string.reader_error) + ": " + e.message)
            return
        } catch (e: Exception) {
            showError(getString(R.string.reader_error) + ": " + e.message)
            return
        }

        if (cursor == null) {
            showError(getString(R.string.reader_error))
            return
        }

        val builder = StringBuilder()
        cursor.use {
            if (it.count == 0) {
                output.text = getString(R.string.reader_empty)
                return
            }
            val titleCol = it.getColumnIndexOrThrow("title")
            val categoryCol = it.getColumnIndexOrThrow("category")
            val playersCol = it.getColumnIndexOrThrow("players")
            val ratingCol = it.getColumnIndexOrThrow("rating")
            var index = 1
            while (it.moveToNext()) {
                builder.append(
                    getString(
                        R.string.reader_item,
                        index++,
                        it.getString(titleCol),
                        it.getString(categoryCol),
                        it.getInt(playersCol),
                        it.getFloat(ratingCol)
                    )
                ).append("\n\n")
            }
        }
        output.text = builder.toString().trim()
    }

    private fun showError(message: String) {
        output.text = message
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val PROVIDER_URI =
            "content://com.example.fominapolinaalekseevna.provider/games"
    }
}
