package com.example.fominapolinaalekseevna

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class ProviderGamesFragment : Fragment() {

    private lateinit var tvProviderOutput: TextView
    private lateinit var etTitle: EditText
    private lateinit var etCategory: EditText
    private lateinit var etPlayers: EditText
    private lateinit var etRating: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_provider_games, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvProviderOutput = view.findViewById(R.id.tvProviderOutput)
        etTitle = view.findViewById(R.id.etProviderTitle)
        etCategory = view.findViewById(R.id.etProviderCategory)
        etPlayers = view.findViewById(R.id.etProviderPlayers)
        etRating = view.findViewById(R.id.etProviderRating)

        view.findViewById<Button>(R.id.btnProviderRefresh).setOnClickListener { loadFromProvider() }
        view.findViewById<Button>(R.id.btnProviderAdd).setOnClickListener { addToProvider() }
        view.findViewById<Button>(R.id.btnOpenReader).setOnClickListener { openReaderApp() }

        loadFromProvider()
    }

    private fun loadFromProvider() {
        val resolver = requireContext().contentResolver
        val projection = arrayOf(
            GameContract.Game._ID,
            GameContract.Game.TITLE,
            GameContract.Game.CATEGORY,
            GameContract.Game.PLAYERS,
            GameContract.Game.RATING
        )
        val cursor: Cursor? = resolver.query(
            GameContract.CONTENT_URI,
            projection,
            null,
            null,
            "${GameContract.Game.TITLE} ASC"
        )
        if (cursor == null) {
            tvProviderOutput.text = getString(R.string.provider_query_error)
            return
        }
        val builder = StringBuilder()
        cursor.use {
            val idCol = it.getColumnIndexOrThrow(GameContract.Game._ID)
            val titleCol = it.getColumnIndexOrThrow(GameContract.Game.TITLE)
            val categoryCol = it.getColumnIndexOrThrow(GameContract.Game.CATEGORY)
            val playersCol = it.getColumnIndexOrThrow(GameContract.Game.PLAYERS)
            val ratingCol = it.getColumnIndexOrThrow(GameContract.Game.RATING)
            var index = 1
            while (it.moveToNext()) {
                builder.append(
                    getString(
                        R.string.provider_item_format,
                        index++,
                        it.getLong(idCol),
                        it.getString(titleCol),
                        it.getString(categoryCol),
                        it.getInt(playersCol),
                        it.getFloat(ratingCol)
                    )
                ).append("\n\n")
            }
        }
        tvProviderOutput.text = if (builder.isEmpty()) {
            getString(R.string.provider_empty)
        } else {
            builder.toString().trim()
        }
    }

    private fun addToProvider() {
        val title = etTitle.text.toString().trim()
        if (title.isEmpty()) {
            Toast.makeText(requireContext(), R.string.json_fill_title, Toast.LENGTH_SHORT).show()
            return
        }
        val values = ContentValues().apply {
            put(GameContract.Game.TITLE, title)
            put(GameContract.Game.CATEGORY, etCategory.text.toString().trim().ifEmpty { "—" })
            put(GameContract.Game.PLAYERS, etPlayers.text.toString().toIntOrNull() ?: 2)
            put(GameContract.Game.RATING, etRating.text.toString().replace(',', '.').toFloatOrNull() ?: 0f)
        }
        val uri = requireContext().contentResolver.insert(GameContract.CONTENT_URI, values)
        if (uri == null) {
            Toast.makeText(requireContext(), R.string.provider_insert_error, Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(requireContext(), R.string.provider_insert_ok, Toast.LENGTH_SHORT).show()
        loadFromProvider()
    }

    private fun openReaderApp() {
        if (isReaderInstalled()) {
            try {
                val byAction = Intent(ACTION_OPEN_READER).apply {
                    setPackage(READER_PACKAGE)
                }
                if (byAction.resolveActivity(requireContext().packageManager) != null) {
                    startActivity(byAction)
                    return
                }

                val byComponent = Intent().apply {
                    component = ComponentName(
                        READER_PACKAGE,
                        "$READER_PACKAGE.MainActivity"
                    )
                }
                startActivity(byComponent)
                return
            } catch (_: ActivityNotFoundException) {
                // откроем запасной экран ниже
            }
        }

        startActivity(Intent(requireContext(), ReaderDemoActivity::class.java))
        Toast.makeText(
            requireContext(),
            if (isReaderInstalled()) R.string.reader_fallback_error else R.string.reader_fallback_opened,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun isReaderInstalled(): Boolean {
        val pm = requireContext().packageManager
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pm.getPackageInfo(READER_PACKAGE, PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                pm.getPackageInfo(READER_PACKAGE, 0)
            }
            true
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }
    }

    companion object {
        const val ACTION_OPEN_READER = "com.example.fominapolinaalekseevna.ACTION_OPEN_READER"
        private const val READER_PACKAGE = "com.example.fominapolinaalekseevna.reader"
    }
}
