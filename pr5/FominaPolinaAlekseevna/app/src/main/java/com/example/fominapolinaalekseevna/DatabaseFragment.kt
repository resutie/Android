package com.example.fominapolinaalekseevna

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class DatabaseFragment : Fragment() {

    private lateinit var etTitle: EditText
    private lateinit var etAuthor: EditText
    private lateinit var etYear: EditText
    private lateinit var etGenre: EditText
    private lateinit var tvOutput: TextView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_database, container, false)

        etTitle = view.findViewById(R.id.et_title)
        etAuthor = view.findViewById(R.id.et_author)
        etYear = view.findViewById(R.id.et_year)
        etGenre = view.findViewById(R.id.et_genre)
        tvOutput = view.findViewById(R.id.tv_db_output)
        dbHelper = DatabaseHelper(requireContext())

        view.findViewById<Button>(R.id.btn_db_add).setOnClickListener { addBook() }
        view.findViewById<Button>(R.id.btn_db_find).setOnClickListener { findBook() }
        view.findViewById<Button>(R.id.btn_db_update).setOnClickListener { updateBook() }
        view.findViewById<Button>(R.id.btn_db_delete).setOnClickListener { deleteBook() }
        view.findViewById<Button>(R.id.btn_db_all).setOnClickListener { showAllBooks() }

        return view
    }

    private fun getBookFromFields(): Book {
        return Book(
            title = etTitle.text.toString().trim(),
            author = etAuthor.text.toString().trim(),
            year = etYear.text.toString().toIntOrNull() ?: 0,
            genre = etGenre.text.toString().trim()
        )
    }

    private fun addBook() {
        val book = getBookFromFields()
        if (book.title.isEmpty()) {
            toast("Введите название книги")
            return
        }
        if (dbHelper.addBook(book)) {
            toast("Книга добавлена")
            showAllBooks()
        } else {
            toast("Ошибка добавления")
        }
    }

    private fun findBook() {
        val title = etTitle.text.toString().trim()
        if (title.isEmpty()) {
            toast("Введите название для поиска")
            return
        }
        val book = dbHelper.findBook(title)
        if (book != null) {
            etAuthor.setText(book.author)
            etYear.setText(book.year.toString())
            etGenre.setText(book.genre)
            tvOutput.text = formatBook(book)
        } else {
            toast("Книга не найдена")
        }
    }

    private fun updateBook() {
        val oldTitle = etTitle.text.toString().trim()
        if (oldTitle.isEmpty()) {
            toast("Введите название книги для обновления")
            return
        }
        val newBook = getBookFromFields()
        if (dbHelper.updateBook(oldTitle, newBook)) {
            toast("Книга обновлена")
            showAllBooks()
        } else {
            toast("Книга не найдена")
        }
    }

    private fun deleteBook() {
        val title = etTitle.text.toString().trim()
        if (title.isEmpty()) {
            toast("Введите название книги для удаления")
            return
        }
        if (dbHelper.deleteBook(title)) {
            toast("Книга удалена")
            showAllBooks()
        } else {
            toast("Книга не найдена")
        }
    }

    private fun showAllBooks() {
        val allBooks = dbHelper.getAllBooks()
        tvOutput.text = if (allBooks.isEmpty()) {
            "Таблица пуста"
        } else {
            allBooks.joinToString("\n\n") { formatBook(it) }
        }
    }

    private fun formatBook(book: Book): String {
        return "ID: ${book.id}\nНазвание: ${book.title}\nАвтор: ${book.author}\nГод: ${book.year}\nЖанр: ${book.genre}"
    }

    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
