package com.example.fominapolinaalekseevna

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_type)
        supportActionBar?.title = getString(R.string.title_spinner)

        val spinner = findViewById<Spinner>(R.id.gameTypeSpinnerActivity)
        val selectedText = findViewById<TextView>(R.id.selectedTypeTextActivity)
        val items = resources.getStringArray(R.array.game_types).toList()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            items
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Короткий вывод выбранного формата партии.
        spinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                selectedText.text = getString(R.string.selected_game_type, items[position])
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) = Unit
        })
    }
}
