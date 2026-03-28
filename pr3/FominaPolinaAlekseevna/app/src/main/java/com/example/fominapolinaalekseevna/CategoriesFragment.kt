package com.example.fominapolinaalekseevna

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class CategoriesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categories = resources.getStringArray(R.array.board_game_categories).toList()
        val listView = view.findViewById<ListView>(R.id.categoriesListView)

        listView.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            categories
        )

        // Переход на экран со списком выбранной категории.
        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(requireContext(), ThirdActivity::class.java)
            intent.putExtra(ThirdActivity.EXTRA_CATEGORY_NAME, categories[position])
            startActivity(intent)
        }
    }
}
