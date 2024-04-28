package com.example.mathsquiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ScoreActivity : AppCompatActivity() {
    private lateinit var finalScoreTextView: TextView
    private lateinit var highScoreTextView: TextView
    private lateinit var playAgainButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        finalScoreTextView = findViewById(R.id.final_score_text_view)
        highScoreTextView = findViewById(R.id.high_score_text_view)
        playAgainButton = findViewById(R.id.play_again_button)

        // Get the final score from the intent
        val finalScore = intent.getIntExtra("FINAL_SCORE", 0)

        // Get the high score from shared preferences
        val highScore = SharedPreferencesHelper.getHighScore(this)

        // Update the high score if the final score is higher
        if (finalScore > highScore) {
            SharedPreferencesHelper.saveHighScore(this, finalScore)
            highScoreTextView.text = "New High Score: $finalScore"
        } else {
            highScoreTextView.text = "High Score: $highScore"
        }

        finalScoreTextView.text = "Final Score: $finalScore"

        // Button to play again
        playAgainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // Start a new game
            finish() // Close the score activity
        }
    }
}
