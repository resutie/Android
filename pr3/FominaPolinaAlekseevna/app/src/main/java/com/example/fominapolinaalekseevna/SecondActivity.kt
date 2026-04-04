package com.example.fominapolinaalekseevna

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val toolbar = findViewById<Toolbar>(R.id.secondToolbar)
        setSupportActionBar(toolbar)
        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { view, insets ->
            val topInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(
                view.paddingLeft,
                topInset,
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        if (savedInstanceState == null) {
            supportActionBar?.title = getString(R.string.bottom_collection)
            supportFragmentManager.beginTransaction()
                .replace(R.id.bottomFragmentContainer, RecyclerGamesFragment())
                .commit()
        }

        bottomNavigation.setOnItemSelectedListener { item -> // ActionBar и контент синхронно меняются по нажатию.
            when (item.itemId) {
                R.id.bottom_collection -> {
                    supportActionBar?.title = getString(R.string.bottom_collection)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.bottomFragmentContainer, RecyclerGamesFragment())
                        .commit()
                    true
                }

                R.id.bottom_top -> {
                    supportActionBar?.title = getString(R.string.bottom_top)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.bottomFragmentContainer, TopGamesFragment())
                        .commit()
                    true
                }

                R.id.bottom_about -> {
                    supportActionBar?.title = getString(R.string.bottom_about)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.bottomFragmentContainer, AboutFragment())
                        .commit()
                    true
                }

                else -> false
            }
        }
    }
}