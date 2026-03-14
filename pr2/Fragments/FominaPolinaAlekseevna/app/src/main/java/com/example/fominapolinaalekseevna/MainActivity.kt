package com.example.fominapolinaalekseevna

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStatic = findViewById<Button>(R.id.btnStatic)
        val btnDynamic = findViewById<Button>(R.id.btnDynamic)
        val btnContainer = findViewById<Button>(R.id.btnContainer)

        showStaticFragment()

        btnStatic.setOnClickListener {
            showStaticFragment()
        }

        btnDynamic.setOnClickListener {
            showDynamicFragment(SecondFragment())
        }

        btnContainer.setOnClickListener {
            showContainerFragment()
        }
    }

    private fun showStaticFragment() {
        val container = findViewById<FrameLayout>(R.id.fragment_container)
        container.removeAllViews()

        val fragmentTag = "static_fragment"
        val staticFragment = supportFragmentManager.findFragmentByTag(fragmentTag)

        if (staticFragment == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FirstFragment(), fragmentTag)
                .commit()
        }
    }

    private fun showDynamicFragment(fragment: Fragment) {
        val container = findViewById<FrameLayout>(R.id.fragment_container)
        container.removeAllViews()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun showContainerFragment() {
        val container = findViewById<FrameLayout>(R.id.fragment_container)
        container.removeAllViews()

        val containerView = FragmentContainerView(this)
        containerView.id = View.generateViewId()
        container.addView(containerView, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT))

        supportFragmentManager.beginTransaction()
            .add(containerView.id, ThirdFragment())
            .commit()
    }
}