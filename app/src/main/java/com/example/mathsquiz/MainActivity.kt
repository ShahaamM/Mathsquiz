package com.example.mathsquiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val additionButton: Button = findViewById(R.id.addition_button)
        val subtractionButton: Button = findViewById(R.id.subtraction_button)
        val multiplicationButton: Button = findViewById(R.id.multiplication_button)

        additionButton.setOnClickListener {
            startQuiz("Addition")
        }

        subtractionButton.setOnClickListener {
            startQuiz("Subtraction")
        }

        multiplicationButton.setOnClickListener {
            startQuiz("Multiplication")
        }

    }

    private fun startQuiz(quizType: String) {
        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra("QUIZ_TYPE", quizType)
        startActivity(intent) // Start the quiz with the selected type
    }
}
