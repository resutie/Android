package com.example.fominapolinaalekseevna

import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Экран Reader внутри основного приложения — запасной вариант,
 * если отдельный модуль reader не установлен на устройстве.
 */
class ReaderDemoActivity : AppCompatActivity() {

    private lateinit var output: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader_demo)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.reader_demo_title)

        output = findViewById(R.id.tvReaderDemoOutput)
        findViewById<Button>(R.id.btnReaderDemoLoad).setOnClickListener { loadFromProvider() }
        loadFromProvider()
    }

    private fun loadFromProvider() {
        output.text = getString(R.string.reader_loading)
        val cursor: Cursor? = try {
            contentResolver.query(
                GameContract.CONTENT_URI,
                arrayOf(
                    GameContract.Game.TITLE,
                    GameContract.Game.CATEGORY,
                    GameContract.Game.PLAYERS,
                    GameContract.Game.RATING
                ),
                null,
                null,
                "${GameContract.Game.TITLE} ASC"
            )
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
            val titleCol = it.getColumnIndexOrThrow(GameContract.Game.TITLE)
            val categoryCol = it.getColumnIndexOrThrow(GameContract.Game.CATEGORY)
            val playersCol = it.getColumnIndexOrThrow(GameContract.Game.PLAYERS)
            val ratingCol = it.getColumnIndexOrThrow(GameContract.Game.RATING)
            var index = 1
            while (it.moveToNext()) {
                builder.append(
                    getString(
                        R.string.reader_item_inline,
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
