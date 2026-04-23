package com.example.fominapolinaalekseevna.reader

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var etFilename: EditText
    private lateinit var tvOutput: TextView

    private val pickDocument = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) {
            Toast.makeText(this, "Файл не выбран", Toast.LENGTH_SHORT).show()
            return@registerForActivityResult
        }
        try {
            contentResolver.openInputStream(uri)?.use { input ->
                tvOutput.text = input.bufferedReader().readText()
            } ?: Toast.makeText(this, "Ошибка чтения файла", Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
            Toast.makeText(this, "Не удалось прочитать выбранный файл", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(this, "Разрешение на чтение не выдано", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Разрешение получено", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etFilename = findViewById(R.id.et_read_filename)
        tvOutput = findViewById(R.id.tv_read_output)

        checkPermissionIfNeeded()

        findViewById<Button>(R.id.btn_read_ext).setOnClickListener {
            readExternalFile()
        }
        findViewById<Button>(R.id.btn_pick_file).setOnClickListener {
            pickDocument.launch(arrayOf("text/plain", "*/*"))
        }
    }

    private fun checkPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun readExternalFile() {
        val filename = etFilename.text.toString().trim()
        if (filename.isEmpty()) {
            Toast.makeText(this, "Введите название файла", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val uri = findDocumentUriByName(filename) ?: findAnyUriByName(filename)
                if (uri == null) {
                    Toast.makeText(
                        this,
                        "Файл не найден по имени. Нажмите \"Выбрать файл вручную\".",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
                contentResolver.openInputStream(uri)?.use { input ->
                    tvOutput.text = input.bufferedReader().readText()
                } ?: run {
                    Toast.makeText(this, "Ошибка чтения файла", Toast.LENGTH_SHORT).show()
                    return
                }
            } else {
                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                val file = File(dir, filename)
                if (!file.exists()) {
                    Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show()
                    return
                }
                tvOutput.text = file.bufferedReader().readText()
            }
        } catch (_: SecurityException) {
            Toast.makeText(
                this,
                "Нет доступа к файлу. Проверь разрешения приложения.",
                Toast.LENGTH_LONG
            ).show()
        } catch (_: IOException) {
            Toast.makeText(this, "Ошибка чтения файла", Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
            Toast.makeText(this, "Не удалось прочитать файл", Toast.LENGTH_SHORT).show()
        }
    }

    private fun findDocumentUriByName(filename: String): Uri? {
        val baseUri = MediaStore.Files.getContentUri("external")
        val projection = arrayOf(MediaStore.MediaColumns._ID)
        val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ? AND (${MediaStore.MediaColumns.RELATIVE_PATH} = ? OR ${MediaStore.MediaColumns.RELATIVE_PATH} = ?)"
        val args = arrayOf(filename, "${Environment.DIRECTORY_DOCUMENTS}/", Environment.DIRECTORY_DOCUMENTS)
        contentResolver.query(baseUri, projection, selection, args, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                return Uri.withAppendedPath(baseUri, id.toString())
            }
        }
        return null
    }

    private fun findAnyUriByName(filename: String): Uri? {
        val baseUri = MediaStore.Files.getContentUri("external")
        val projection = arrayOf(MediaStore.MediaColumns._ID)
        val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ?"
        val args = arrayOf(filename)
        val sort = "${MediaStore.MediaColumns.DATE_MODIFIED} DESC"
        contentResolver.query(baseUri, projection, selection, args, sort)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                return Uri.withAppendedPath(baseUri, id.toString())
            }
        }
        return null
    }
}
