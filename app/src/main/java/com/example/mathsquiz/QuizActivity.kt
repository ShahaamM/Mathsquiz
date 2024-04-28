package com.example.mathsquiz

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class QuizActivity : AppCompatActivity() {
    private lateinit var questionTextView: TextView
    private lateinit var option1Button: Button
    private lateinit var option2Button: Button
    private lateinit var option3Button: Button
    private lateinit var option4Button: Button
    private lateinit var scoreTextView: TextView
    private lateinit var timerTextView: TextView
    private lateinit var questionCounterTextView: TextView
    private var score = 0
    private var questionIndex = 0
    private var totalQuestions = 20
    private var timer: CountDownTimer? = null
    private lateinit var questions: List<Question>
    private var currentQuestion: Question? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Initialize UI components
        questionTextView = findViewById(R.id.question_text_view)
        option1Button = findViewById(R.id.option1_button)
        option2Button = findViewById(R.id.option2_button)
        option3Button = findViewById(R.id.option3_button)
        option4Button = findViewById(R.id.option4_button)
        scoreTextView = findViewById(R.id.score_text_view)
        timerTextView = findViewById(R.id.timer_text_view)
        questionCounterTextView = findViewById(R.id.question_counter_text_view)

        // Get the selected quiz type from the intent
        val quizType = intent.getStringExtra("QUIZ_TYPE")

        // Generate 20 random questions based on the selected quiz type
        questions = generateQuestions(quizType!!, totalQuestions)

        // Start the quiz with the first question
        showNextQuestion()

        // Set click listeners for option buttons
        option1Button.setOnClickListener { onOptionSelected(it.id) }
        option2Button.setOnClickListener { onOptionSelected(it.id) }
        option3Button.setOnClickListener { onOptionSelected(it.id) }
        option4Button.setOnClickListener { onOptionSelected(it.id) }

        updateScoreDisplay() // Update the score display
    }

    private fun generateQuestions(quizType: String, count: Int): List<Question> {
        val questions = mutableListOf<Question>()
        for (i in 0 until count) {
            questions.add(generateRandomQuestion(quizType))
        }
        return questions
    }

    private fun generateRandomQuestion(quizType: String): Question {
        val num1 = Random.nextInt(100)
        val num2 = Random.nextInt(100)
        val questionText: String
        val correctAnswer: Int

        when (quizType) {
            "Addition" -> {
                questionText = "$num1 + $num2"
                correctAnswer = num1 + num2
            }
            "Subtraction" -> {
                questionText = "$num1 - $num2"
                correctAnswer = num1 - num2
            }
            "Multiplication" -> {
                questionText = "$num1 * $num2"
                correctAnswer = num1 * num2
            }
            "Division" -> {
                questionText = "$num1 / $num2"
                correctAnswer = num1 / num2
            }
            else -> {
                throw IllegalArgumentException("Invalid quiz type")
            }
        }

        val options = generateOptions(correctAnswer)
        return Question(questionText, options, options.indexOf(correctAnswer.toString()))
    }

    private fun generateOptions(correctAnswer: Int): List<String> {
        val options = mutableListOf(correctAnswer.toString())
        while (options.size < 4) {
            val wrongAnswer = correctAnswer + Random.nextInt(1, 10) - 5
            if (!options.contains(wrongAnswer.toString())) {
                options.add(wrongAnswer.toString())
            }
        }
        return options.shuffled()
    }

    private fun showNextQuestion() {
        if (questionIndex >= totalQuestions) {
            endQuiz() // End the quiz if all questions are answered
        } else {
            currentQuestion = questions[questionIndex]
            updateQuestionUI() // Update the question and options
            startTimer() // Start the timer for the current question
            questionIndex++
        }
    }

    private fun updateQuestionUI() {
        currentQuestion?.let {
            questionTextView.text = it.question
            option1Button.text = it.options[0]
            option2Button.text = it.options[1]
            option3Button.text = it.options[2]
            option4Button.text = it.options[3]
        }
    }

    private fun startTimer() {
        timer?.cancel() // Cancel any existing timer
        timer = object : CountDownTimer(10000, 100) { // 10 seconds with 100 ms intervals
            override fun onTick(millisUntilFinished: Long) {
                timerTextView.text = "${millisUntilFinished / 1000}" // Display seconds remaining
            }

            override fun onFinish() {
                Toast.makeText(this@QuizActivity, "Time's up!", Toast.LENGTH_SHORT).show()
                endQuiz() // End the quiz if the timer runs out
            }
        }
        timer?.start() // Start the new timer
    }

    private fun onOptionSelected(buttonId: Int) {
        val selectedOption = when (buttonId) {
            R.id.option1_button -> 0
            R.id.option2_button -> 1
            R.id.option3_button -> 2
            R.id.option4_button -> 3
            else -> return
        }

        if (selectedOption == currentQuestion?.correctOption) {
            score += 10
            Toast.makeText(this@QuizActivity, "Correct!", Toast.LENGTH_SHORT).show() // Correct answer
        } else {
            Toast.makeText(this@QuizActivity, "Incorrect!", Toast.LENGTH_SHORT).show() // Incorrect answer
            endQuiz() // End the quiz on incorrect answer
            return
        }

        updateScoreDisplay() // Update the score and high score display

        showNextQuestion() // Move to the next question
    }

    private fun updateScoreDisplay() {
        val highScore = SharedPreferencesHelper.getHighScore(this)

        if (score > highScore) {
            SharedPreferencesHelper.saveHighScore(this, score) // Save new high score
        }

        scoreTextView.text = "Score: $score | High Score: $highScore" // Update score display
    }

    private fun endQuiz() {
        timer?.cancel() // Cancel the timer if it's running

        // Start ScoreActivity with the final score
        val intent = Intent(this, ScoreActivity::class.java)
        intent.putExtra("FINAL_SCORE", score) // Pass the final score
        startActivity(intent) // Start the score activity

        finish() // Finish the quiz activity
    }

}
