package com.example.fominapolinaalekseevna

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.jvm.java


class MainActivity : AppCompatActivity() {
    private lateinit var fioEditText: EditText
    private lateinit var groupEditText: EditText
    private lateinit var ageEditText: EditText
    private val TAG = "mylogs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.d(TAG, "onCreate вызван")

        val progButton = findViewById<Button?>(R.id.progButton)
        fioEditText = findViewById<EditText?>(R.id.fioEditText)
        groupEditText = findViewById<EditText?>(R.id.groupEditText)
        ageEditText = findViewById<EditText?>(R.id.ageEditText)

        progButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onNextActivity(v)
            }
        })
    }

    fun onNextActivity(view: View?) {
        val intent: Intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("fio", fioEditText.getText());
        intent.putExtra("group", groupEditText.getText());
        intent.putExtra("age", ageEditText.getText());
        startActivity(intent)
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart вызван")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume вызван")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause вызван")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop вызван")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy вызван")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart вызван")
    }
}