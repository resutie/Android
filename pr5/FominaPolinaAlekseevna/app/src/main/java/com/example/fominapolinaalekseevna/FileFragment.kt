package com.example.fominapolinaalekseevna

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class FileFragment : Fragment() {

    companion object {
        private const val KEY_FILENAME = "key_filename"
        private const val KEY_CONTENT = "key_content"
        private const val KEY_OUTPUT = "key_output"
    }

    private lateinit var etFilename: EditText
    private lateinit var etContent: EditText
    private lateinit var tvOutput: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_file, container, false)

        etFilename = view.findViewById(R.id.et_filename)
        etContent = view.findViewById(R.id.et_content)
        tvOutput = view.findViewById(R.id.tv_output)

        view.findViewById<Button>(R.id.btn_save).setOnClickListener { saveFile() }
        view.findViewById<Button>(R.id.btn_append).setOnClickListener { appendToFile() }
        view.findViewById<Button>(R.id.btn_read).setOnClickListener { readFile() }
        view.findViewById<Button>(R.id.btn_delete).setOnClickListener { confirmDelete() }

        if (savedInstanceState != null) {
            etFilename.setText(savedInstanceState.getString(KEY_FILENAME, ""))
            etContent.setText(savedInstanceState.getString(KEY_CONTENT, ""))
            tvOutput.text = savedInstanceState.getString(KEY_OUTPUT, "")
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_FILENAME, etFilename.text.toString())
        outState.putString(KEY_CONTENT, etContent.text.toString())
        outState.putString(KEY_OUTPUT, tvOutput.text.toString())
    }

    private fun saveFile() {
        val filename = etFilename.text.toString().trim()
        val content = etContent.text.toString()
        if (filename.isEmpty()) {
            toast("Введите название файла")
            return
        }
        if (content.isEmpty()) {
            toast("Введите содержимое файла")
            return
        }

        try {
            requireContext().openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(content.toByteArray())
            }
            toast("Файл сохранен")
        } catch (e: IOException) {
            toast("Ошибка сохранения: ${e.message}")
        }
    }

    private fun appendToFile() {
        val filename = etFilename.text.toString().trim()
        val content = etContent.text.toString()
        if (filename.isEmpty()) {
            toast("Введите название файла")
            return
        }
        if (content.isEmpty()) {
            toast("Введите содержимое для дозаписи")
            return
        }

        try {
            requireContext().openFileOutput(filename, Context.MODE_APPEND).use {
                it.write(content.toByteArray())
            }
            toast("Новый текст добавлен в конец файла")
        } catch (e: IOException) {
            toast("Ошибка записи: ${e.message}")
        }
    }

    private fun readFile() {
        val filename = etFilename.text.toString().trim()
        if (filename.isEmpty()) {
            toast("Введите название файла")
            return
        }

        try {
            requireContext().openFileInput(filename).use { stream ->
                tvOutput.text = stream.bufferedReader().readText()
            }
        } catch (_: FileNotFoundException) {
            toast("Файл не найден")
        } catch (e: IOException) {
            toast("Ошибка чтения: ${e.message}")
        }
    }

    private fun confirmDelete() {
        val filename = etFilename.text.toString().trim()
        if (filename.isEmpty()) {
            toast("Введите название файла")
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Удаление файла")
            .setMessage("Вы уверены, что хотите удалить файл \"$filename\"?")
            .setPositiveButton("Да") { _, _ -> deleteFile(filename) }
            .setNegativeButton("Нет", null)
            .show()
    }

    private fun deleteFile(filename: String) {
        val target = File(requireContext().filesDir, filename)
        if (target.exists() && target.delete()) {
            tvOutput.text = ""
            toast("Файл удален")
        } else {
            toast("Не удалось удалить файл")
        }
    }

    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
