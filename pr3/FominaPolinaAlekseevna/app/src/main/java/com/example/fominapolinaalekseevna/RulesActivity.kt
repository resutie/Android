package com.example.fominapolinaalekseevna

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class RulesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)
        supportActionBar?.title = getString(R.string.title_scroll)
    }
}
