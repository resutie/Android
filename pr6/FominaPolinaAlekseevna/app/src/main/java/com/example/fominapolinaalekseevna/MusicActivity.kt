package com.example.fominapolinaalekseevna

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

class MusicActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var playButton: Button
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_music)

        playButton = findViewById(R.id.btnPlayMusic)
        statusText = findViewById(R.id.tvMusicStatus)

        playButton.isEnabled = false
        playButton.setOnClickListener { togglePlayback() }
        findViewById<Button>(R.id.btnRetryMusic).setOnClickListener { preparePlayer() }

        preparePlayer()
    }

    private fun preparePlayer() {
        releasePlayer()
        playButton.isEnabled = false
        playButton.text = getString(R.string.music_play)
        statusText.setText(R.string.music_loading)

        val player = MediaPlayer()
        mediaPlayer = player

        player.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )

        player.setOnPreparedListener {
            playButton.isEnabled = true
            statusText.setText(R.string.music_ready)
        }

        player.setOnCompletionListener {
            playButton.text = getString(R.string.music_play)
            statusText.setText(R.string.music_ready)
        }

        player.setOnErrorListener { _, what, extra ->
            playButton.isEnabled = false
            statusText.setText(R.string.music_error)
            Toast.makeText(
                this,
                getString(R.string.music_error_detail, what, extra),
                Toast.LENGTH_LONG
            ).show()
            true
        }

        try {
            player.setDataSource(MUSIC_URL)
            player.prepareAsync()
        } catch (_: IOException) {
            playButton.isEnabled = false
            statusText.setText(R.string.music_error)
            Toast.makeText(this, R.string.music_error, Toast.LENGTH_LONG).show()
        } catch (_: IllegalArgumentException) {
            playButton.isEnabled = false
            statusText.setText(R.string.music_error)
            Toast.makeText(this, R.string.music_error, Toast.LENGTH_LONG).show()
        }
    }

    private fun togglePlayback() {
        val player = mediaPlayer ?: return
        if (!player.isPlaying) {
            player.start()
            playButton.text = getString(R.string.music_pause)
            statusText.setText(R.string.music_playing)
        } else {
            player.pause()
            playButton.text = getString(R.string.music_play)
            statusText.setText(R.string.music_paused)
        }
    }

    private fun releasePlayer() {
        mediaPlayer?.run {
            if (isPlaying) stop()
            release()
        }
        mediaPlayer = null
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        // Публичный MP3 — прямой URL для MediaPlayer (как в методичке)
        private const val MUSIC_URL =
            "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3"
    }
}
