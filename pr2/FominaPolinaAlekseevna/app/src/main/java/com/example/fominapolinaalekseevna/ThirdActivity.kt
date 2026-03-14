package com.example.fominapolinaalekseevna

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_third)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dayEditText = findViewById<EditText>(R.id.dayEditText)
        val timeEditText = findViewById<EditText>(R.id.timeEditText)
        val commentEditText = findViewById<EditText>(R.id.commentEditText)
        val okButton = findViewById<Button>(R.id.okButton)

        val subject = intent.getStringExtra("subject")

        okButton.setOnClickListener {
            val resultIntent = Intent()
            val visitInfo = getString(R.string.visit_info_format,
                subject ?: "",
                dayEditText.text.toString(),
                timeEditText.text.toString(),
                commentEditText.text.toString())
            resultIntent.putExtra("visitInfo", visitInfo)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}