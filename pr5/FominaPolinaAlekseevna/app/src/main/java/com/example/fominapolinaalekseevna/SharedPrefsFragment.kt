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
import androidx.fragment.app.Fragment

class SharedPrefsFragment : Fragment() {

    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val KEY_USERNAME = "username"
    }

    private lateinit var etUsername: EditText
    private lateinit var tvOutput: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_shared_prefs, container, false)
        etUsername = view.findViewById(R.id.et_username)
        tvOutput = view.findViewById(R.id.tv_prefs_output)

        view.findViewById<Button>(R.id.btn_save_prefs).setOnClickListener { saveUsername() }
        view.findViewById<Button>(R.id.btn_get_prefs).setOnClickListener { getUsername() }
        view.findViewById<Button>(R.id.btn_update_prefs).setOnClickListener { updateUsername() }
        view.findViewById<Button>(R.id.btn_delete_prefs).setOnClickListener { deleteUsername() }

        return view
    }

    private fun prefs() = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private fun saveUsername() {
        val name = etUsername.text.toString().trim()
        if (name.isEmpty()) {
            toast("Введите имя пользователя")
            return
        }
        prefs().edit().putString(KEY_USERNAME, name).apply()
        toast("Имя сохранено")
    }

    private fun getUsername() {
        val savedName = prefs().getString(KEY_USERNAME, "Не задано")
        tvOutput.text = "Имя пользователя: $savedName"
    }

    private fun updateUsername() {
        val name = etUsername.text.toString().trim()
        if (name.isEmpty()) {
            toast("Введите новое имя пользователя")
            return
        }
        prefs().edit().putString(KEY_USERNAME, name).apply()
        tvOutput.text = "Имя изменено: $name"
        toast("Имя обновлено")
    }

    private fun deleteUsername() {
        prefs().edit().remove(KEY_USERNAME).apply()
        tvOutput.text = "Имя удалено"
        toast("Данные удалены")
    }

    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
