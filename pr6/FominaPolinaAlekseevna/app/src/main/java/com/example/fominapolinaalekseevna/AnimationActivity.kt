package com.example.fominapolinaalekseevna

import android.animation.ObjectAnimator
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AnimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_animations)

        val rotateImage = findViewById<ImageView>(R.id.rotateImageView)
        val moveButton = findViewById<Button>(R.id.moveButton)
        val scaleText = findViewById<TextView>(R.id.scaleTextView)

        ObjectAnimator.ofFloat(rotateImage, "rotation", 0f, 360f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
            start()
        }

        moveButton.setOnClickListener {
            moveButton.translationX = 0f
            ObjectAnimator.ofFloat(moveButton, "translationX", 0f, 300f).apply {
                duration = 1000
                start()
            }
        }

        scaleText.setOnClickListener {
            ObjectAnimator.ofFloat(scaleText, "scaleX", 1f, 2f).apply {
                duration = 1000
                start()
            }
            ObjectAnimator.ofFloat(scaleText, "scaleY", 1f, 2f).apply {
                duration = 1000
                start()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
