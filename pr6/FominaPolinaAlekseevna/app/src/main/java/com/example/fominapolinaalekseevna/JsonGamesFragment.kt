package com.example.fominapolinaalekseevna

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import java.io.File
import java.io.IOException

class JsonGamesFragment : Fragment() {

    private lateinit var etTitle: EditText
    private lateinit var etCategory: EditText
    private lateinit var etPlayers: EditText
    private lateinit var etRating: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_json_games, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etTitle = view.findViewById(R.id.etJsonTitle)
        etCategory = view.findViewById(R.id.etJsonCategory)
        etPlayers = view.findViewById(R.id.etJsonPlayers)
        etRating = view.findViewById(R.id.etJsonRating)

        view.findViewById<Button>(R.id.btnSaveJson).setOnClickListener { saveToJsonFile() }
        view.findViewById<Button>(R.id.btnLoadJson).setOnClickListener { loadFromJsonFile() }
    }

    private fun saveToJsonFile() {
        val record = readFieldsFromUi()
        if (record.title.isBlank()) {
            Toast.makeText(requireContext(), R.string.json_fill_title, Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val json = Gson().toJson(record)
            jsonFile().writeText(json)
            Toast.makeText(
                requireContext(),
                getString(R.string.json_saved, jsonFile().absolutePath),
                Toast.LENGTH_LONG
            ).show()
        } catch (_: IOException) {
            Toast.makeText(requireContext(), R.string.json_save_error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadFromJsonFile() {
        val file = jsonFile()
        if (!file.exists()) {
            Toast.makeText(requireContext(), R.string.json_file_missing, Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val record = Gson().fromJson(file.readText(), GameRecord::class.java)
            fillFields(record)
            Toast.makeText(requireContext(), R.string.json_loaded, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
            Toast.makeText(requireContext(), R.string.json_load_error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun readFieldsFromUi(): GameRecord {
        val players = etPlayers.text.toString().toIntOrNull() ?: 2
        val rating = etRating.text.toString().replace(',', '.').toFloatOrNull() ?: 0f
        return GameRecord(
            title = etTitle.text.toString().trim(),
            category = etCategory.text.toString().trim(),
            players = players,
            rating = rating
        )
    }

    private fun fillFields(record: GameRecord) {
        etTitle.setText(record.title)
        etCategory.setText(record.category)
        etPlayers.setText(record.players.toString())
        etRating.setText(record.rating.toString())
    }

    private fun jsonFile(): File = File(requireContext().filesDir, JSON_FILE_NAME)

    companion object {
        private const val JSON_FILE_NAME = "game_record.json"
    }
}
