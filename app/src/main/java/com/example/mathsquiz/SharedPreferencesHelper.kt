package com.example.mathsquiz

import android.content.Context

object SharedPreferencesHelper {
    private const val PREF_NAME = "MathsQuizPreferences"
    private const val HIGH_SCORE_KEY = "HighScore"

    // Save a new high score
    fun saveHighScore(context: Context, highScore: Int) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(HIGH_SCORE_KEY, highScore)
        editor.apply()
    }

    // Get the current high score
    fun getHighScore(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(HIGH_SCORE_KEY, 0) // Default to 0 if no high score
    }
}
