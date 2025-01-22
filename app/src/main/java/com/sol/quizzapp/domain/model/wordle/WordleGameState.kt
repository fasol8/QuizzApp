package com.sol.quizzapp.domain.model.wordle

data class WordleGameState(
    val targetWord: String,
    val guesses: List<String> = emptyList(),
    val feedback: List<List<String>> = emptyList(),
    val currentGuess: String = "",
    val gameFinished: Boolean = false,
    val gameWon: Boolean = false,
    val attempts: Int = 0,
    val maxAttempts: Int = 8
)