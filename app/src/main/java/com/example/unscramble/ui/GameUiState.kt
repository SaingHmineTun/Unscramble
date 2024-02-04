package com.example.unscramble.ui

data class GameUiState (
    val unit: Int = 0,
    val guessScramble: String = "",
    val typedScramble: String = "",
    val score: Int = 0,
    val guessWrong: Boolean = false
)