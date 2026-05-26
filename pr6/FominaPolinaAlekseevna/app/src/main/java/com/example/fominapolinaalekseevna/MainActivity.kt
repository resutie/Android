package com.example.fominapolinaalekseevna

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.mainToolbar)
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

        drawerLayout = findViewById(R.id.drawerLayout)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)

        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_categories)

        warmupContentProvider()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CategoriesFragment())
                .commit()
            navigationView.setCheckedItem(R.id.menu_categories)
        }

        navigationView.setNavigationItemSelectedListener { item -> // Меняем фрагменты по пунктам бокового меню.
            when (item.itemId) {
                R.id.menu_categories -> {
                    supportActionBar?.title = getString(R.string.title_categories)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, CategoriesFragment())
                        .commit()
                }

                R.id.menu_scroll -> {
                    startActivity(Intent(this, RulesActivity::class.java))
                }

                R.id.menu_spinner -> {
                    startActivity(Intent(this, GameTypeActivity::class.java))
                }

                R.id.menu_bottom_activity -> {
                    startActivity(Intent(this, SecondActivity::class.java))
                }

                R.id.menu_webview -> {
                    startActivity(Intent(this, WebViewActivity::class.java))
                }

                R.id.menu_music -> {
                    startActivity(Intent(this, MusicActivity::class.java))
                }

                R.id.menu_animations -> {
                    startActivity(Intent(this, AnimationActivity::class.java))
                }

                R.id.menu_json -> {
                    supportActionBar?.title = getString(R.string.title_json)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, JsonGamesFragment())
                        .commit()
                }

                R.id.menu_provider -> {
                    supportActionBar?.title = getString(R.string.title_provider)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, ProviderGamesFragment())
                        .commit()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun warmupContentProvider() {
        contentResolver.query(GameContract.CONTENT_URI, null, null, null, null)?.close()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}