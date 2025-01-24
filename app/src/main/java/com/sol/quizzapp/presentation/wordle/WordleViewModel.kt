package com.sol.quizzapp.presentation.wordle

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.quizzapp.data.local.wordle.WordleEntity
import com.sol.quizzapp.domain.model.wordle.WordleGameState
import com.sol.quizzapp.domain.us.WordUS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordleViewModel @Inject constructor(private val getWordUS: WordUS) : ViewModel() {

    private val _gameState = MutableStateFlow(WordleGameState(targetWord = "-----"))
    val gameState: StateFlow<WordleGameState> = _gameState

    private var _resultSaved = MutableStateFlow(false)

    fun startNewGame(category: String) {
        resetResultSaved()
        loadRandomWord(category)
    }

    private fun loadRandomWord(category: String) {
        viewModelScope.launch {
            try {
                val maxAttempts = when (category) {
                    "easy" -> 10
                    "medium" -> 8
                    "hard" -> 6
                    else -> 8
                }
                val length = when (category) {
                    "easy" -> (3..4).random()
                    "medium" -> (5..6).random()
                    "hard" -> (7..9).random()
                    else -> (3..10).random()
                }
                val response = getWordUS.getWordLength(length)
                if (response.isNotEmpty()) {
                    _gameState.value = WordleGameState(
                        targetWord = response.first(),
                        maxAttempts = maxAttempts
                    )
                } else {
                    Log.e("ViewModel", "API response was empty.")
                }
            } catch (e: Exception) {
                Log.i("Error", e.message.toString())
            }
        }
    }

    fun makeGuess(guess: String) {
        val gameState = _gameState.value
        if (guess.length != gameState.targetWord.length || gameState.gameFinished) return

        val result = evaluateGuess(guess)
        val newGuesses = gameState.guesses + guess
        val newFeedback = gameState.feedback + listOf(result)

        if (guess == gameState.targetWord) {

            _gameState.value = gameState.copy(
                gameFinished = true,
                gameWon = true,
                attempts = newGuesses.size,
                guesses = newGuesses,
                feedback = newFeedback
            )
        } else if (newGuesses.size >= gameState.maxAttempts) {
            _gameState.value = gameState.copy(
                gameFinished = true,
                gameWon = false,
                attempts = newGuesses.size,
                guesses = newGuesses,
                feedback = newFeedback
            )
        } else {
            _gameState.value = gameState.copy(
                currentGuess = "",
                guesses = newGuesses,
                feedback = newFeedback
            )
        }
    }

    private fun evaluateGuess(guess: String): List<String> {
        val targetWord = _gameState.value.targetWord
        val result =
            MutableList(guess.length) { "absent" }
        val targetChars =
            targetWord.toMutableList()

        guess.forEachIndexed { index, char ->
            if (char == targetWord[index]) {
                result[index] = "correct"
                targetChars[index] = '_'
            }
        }

        guess.forEachIndexed { index, char ->
            if (result[index] == "absent") {
                val targetIndex = targetChars.indexOf(char)
                if (targetIndex != -1) {
                    result[index] = "present"
                    targetChars[targetIndex] = '_'
                }
            }
        }
        return result
    }

    fun updateCurrentGuess(newGuess: String) {
        _gameState.value = _gameState.value.copy(currentGuess = newGuess)
    }

    fun saveResult(word: String, difficulty: String, attempts: Int, gameWon: Boolean) {
        if (_resultSaved.value) return

        viewModelScope.launch {
            try {
                getWordUS.insertWordle(
                    WordleEntity(
                        gameWon = gameWon,
                        word = word,
                        attempts = attempts,
                        difficulty = difficulty,
                    )
                )
                _resultSaved.value = true
            } catch (e: Exception) {
                Log.i("Error", e.message.toString())
            }
        }
    }

    private fun resetResultSaved() {
        _resultSaved.value = false
    }
}