package com.example.fominapolinaalekseevna

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fioTextView = findViewById<TextView?>(R.id.fioTextView)
        val groupTextView = findViewById<TextView?>(R.id.groupTextView)
        val ageTextView = findViewById<TextView?>(R.id.ageTextView)
        val subjectEditText = findViewById<TextView?>(R.id.subjectEditText)
        val enterButton = findViewById<Button?>(R.id.enterButton)
        val resultTextView = findViewById<TextView?>(R.id.resultTextView)

        val arguments = intent.extras
        val fio: String? = arguments!!.get("fio").toString()
        val group: String? = arguments!!.get("group").toString()
        val age: String? = arguments!!.get("age").toString()

        fioTextView.text = fio
        groupTextView.text = group
        ageTextView.text = age

        enterButton.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            intent.putExtra("subject", subjectEditText.text.toString())
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val visitInfo = data?.getStringExtra("visitInfo")
            findViewById<TextView>(R.id.resultTextView).text = visitInfo
            Toast.makeText(this, getString(R.string.toast_success), Toast.LENGTH_LONG).show()
        }
    }
}