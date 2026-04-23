package com.example.fominapolinaalekseevna

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileWriter
import java.io.IOException

class ExternalStorageFragment : Fragment() {

    private lateinit var etFilename: EditText
    private lateinit var etContent: EditText
    private lateinit var tvOutput: TextView

    private val requestStoragePermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            toast("Разрешение на внешнюю память не выдано")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_external, container, false)

        etFilename = view.findViewById(R.id.et_ext_filename)
        etContent = view.findViewById(R.id.et_ext_content)
        tvOutput = view.findViewById(R.id.tv_ext_output)

        checkPermissionIfNeeded()

        view.findViewById<Button>(R.id.btn_ext_save).setOnClickListener { saveExternal() }
        view.findViewById<Button>(R.id.btn_ext_delete).setOnClickListener { deleteExternal() }

        return view
    }

    private fun checkPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            val granted = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                requestStoragePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun getExternalFile(filename: String): File {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return File(dir, filename)
    }

    private fun saveExternal() {
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveViaMediaStore(filename, content)
            } else {
                val file = getExternalFile(filename)
                FileWriter(file, false).use { writer ->
                    writer.write(content)
                }
                tvOutput.text = "Файл сохранен: ${file.absolutePath}"
            }
            toast("Файл записан во внешнюю память")
        } catch (e: IOException) {
            toast("Ошибка записи: ${e.message}")
        }
    }

    private fun deleteExternal() {
        val filename = etFilename.text.toString().trim()
        if (filename.isEmpty()) {
            toast("Введите название файла")
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (deleteViaMediaStore(filename)) {
                tvOutput.text = ""
                toast("Файл удален из внешней памяти")
            } else {
                toast("Файл не найден или не удален")
            }
        } else {
            val file = getExternalFile(filename)
            if (file.exists() && file.delete()) {
                tvOutput.text = ""
                toast("Файл удален из внешней памяти")
            } else {
                toast("Файл не найден или не удален")
            }
        }
    }

    private fun saveViaMediaStore(filename: String, content: String) {
        val resolver = requireContext().contentResolver
        val existingUri = findDocumentUriByName(filename)
        val targetUri = existingUri ?: resolver.insert(
            MediaStore.Files.getContentUri("external"),
            ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DOCUMENTS}/")
            }
        )

        if (targetUri == null) {
            throw IOException("Не удалось создать запись в MediaStore")
        }

        resolver.openOutputStream(targetUri, "wt")?.use { stream ->
            stream.write(content.toByteArray())
        } ?: throw IOException("Не удалось открыть поток записи")

        tvOutput.text = "Файл сохранен в Documents: $filename"
    }

    private fun deleteViaMediaStore(filename: String): Boolean {
        val resolver = requireContext().contentResolver
        val uri = findDocumentUriByName(filename) ?: return false
        return resolver.delete(uri, null, null) > 0
    }

    private fun findDocumentUriByName(filename: String): Uri? {
        val resolver = requireContext().contentResolver
        val baseUri = MediaStore.Files.getContentUri("external")
        val projection = arrayOf(MediaStore.MediaColumns._ID)
        val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ? AND (${MediaStore.MediaColumns.RELATIVE_PATH} = ? OR ${MediaStore.MediaColumns.RELATIVE_PATH} = ?)"
        val args = arrayOf(filename, "${Environment.DIRECTORY_DOCUMENTS}/", Environment.DIRECTORY_DOCUMENTS)

        resolver.query(baseUri, projection, selection, args, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                return Uri.withAppendedPath(baseUri, id.toString())
            }
        }
        return null
    }

    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
