package com.example.fominapolinaalekseevna

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class ThirdActivity : AppCompatActivity() {
    private val games = mutableListOf<String>()
    private val selectedGames = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        val categoryName = intent.getStringExtra(EXTRA_CATEGORY_NAME).orEmpty()
        val normalized = categoryName.lowercase(Locale.getDefault())

        supportActionBar?.title = categoryName

        val titleText = findViewById<TextView>(R.id.categoryTitleText)
        val gameInput = findViewById<EditText>(R.id.gameInputEditText)
        val addButton = findViewById<Button>(R.id.addGameButton)
        val removeButton = findViewById<Button>(R.id.removeSelectedButton)
        val listView = findViewById<ListView>(R.id.gamesListView)
        val hintText = findViewById<TextView>(R.id.editHintText)

        titleText.text = getString(R.string.games_of_category, categoryName)
        games.addAll(getGamesForCategory(normalized))

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, games)
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selected = adapter.getItem(position) ?: return@setOnItemClickListener
            if (listView.isItemChecked(position)) {
                selectedGames.add(selected)
            } else {
                selectedGames.remove(selected)
            }
        }

        hintText.text = getString(R.string.edit_enabled_all_hint)
        addButton.setOnClickListener {
            val newGame = gameInput.text.toString().trim()
            if (newGame.isNotEmpty()) {
                adapter.add(newGame)
                adapter.notifyDataSetChanged()
                gameInput.text.clear()
            }
        }
        removeButton.setOnClickListener {
            selectedGames.forEach { adapter.remove(it) }
            selectedGames.clear()
            listView.clearChoices()
            adapter.notifyDataSetChanged()
        }
    }

    private fun getGamesForCategory(category: String): List<String> {
        return when (category) {
            CATEGORY_STRATEGY -> listOf("Шахматы", "Каркассон", "7 Чудес", "Сумеречная борьба")
            "кооперативные" -> listOf("Пандемия", "Запретный остров", "Hanabi")
            "семейные" -> listOf("Ticket to Ride", "Catan", "Dixit")
            else -> listOf("Alias", "Uno", "Мафия")
        }
    }

    companion object {
        const val EXTRA_CATEGORY_NAME = "extra_category_name"
        private const val CATEGORY_STRATEGY = "стратегии"
    }
}