package com.sol.quizzapp.presentation.flag

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.quizzapp.data.local.flag.FlagEntity
import com.sol.quizzapp.domain.model.flags.Countries
import com.sol.quizzapp.domain.model.flags.Question
import com.sol.quizzapp.domain.us.FlagUS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlagViewModel @Inject constructor(private val getFlagUS: FlagUS) : ViewModel() {

    private val totalRounds = 10
    private val countries = Countries.values()

    private val _currentQuestion = MutableStateFlow<Question?>(null)
    val currentQuestion: StateFlow<Question?> = _currentQuestion

    private val _currentRound = MutableStateFlow(1)
    val currentRound: StateFlow<Int> = _currentRound

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _quizFinished = MutableStateFlow(false)
    val quizFinished: StateFlow<Boolean> = _quizFinished

    private val _remainingTime = MutableStateFlow(15)
    val remainingTime: StateFlow<Int> = _remainingTime

    private val _selectedAnswer = MutableStateFlow<Countries?>(null)
    val selectedAnswer: StateFlow<Countries?> = _selectedAnswer

    private val _nextButtonEnabled = MutableStateFlow(false)
    val nextButtonEnabled: StateFlow<Boolean> = _nextButtonEnabled

    private val _timeExpired = MutableStateFlow(false)
    val timeExpired: StateFlow<Boolean> get() = _timeExpired

    private var resultSaved = MutableStateFlow(false)
    private val usedCountries = mutableListOf<Countries>()
    private var timerJob: Job? = null

    init {
        loadNextQuestion()
        resetResultSaved()
    }

    private fun generateQuestion(): Question {
        val availableCountries = Countries.values().toList().filter { it !in usedCountries }
        val correctCountry = countries.random()
        usedCountries.add(correctCountry)
        val incorrectOptions = availableCountries.shuffled().take(3)
        val allOptions = (listOf(correctCountry) + incorrectOptions).shuffled()

        return Question(correctCountry, allOptions)
    }

    private fun loadNextQuestion() {
        if (_currentRound.value > totalRounds) {
            _quizFinished.value = true
        } else {
            _currentQuestion.value = generateQuestion()
            _remainingTime.value = 15
            _timeExpired.value = false
            _selectedAnswer.value = null
            _nextButtonEnabled.value = false
        }
    }

    fun decrementTimer() {
        if (_selectedAnswer.value == null && _remainingTime.value > 0) {
            _remainingTime.value -= 1
        }

        if (_remainingTime.value == 0) {
            onTimeExpired()
        }
    }

    private fun onTimeExpired() {
        _timeExpired.value = true
        _nextButtonEnabled.value = true
        _selectedAnswer.value = null
    }

    fun onAnswerSelected(selectedCountry: Countries) {
        if (_timeExpired.value == true) return

        _selectedAnswer.value = selectedCountry

        if (selectedCountry == _currentQuestion.value?.correctCountry) {
            _score.value += 1
        }

        _nextButtonEnabled.value = true
        timerJob?.cancel()
    }

    fun onNextClicked() {
        _currentRound.value += 1
        loadNextQuestion()
    }

    fun saveResult(correctAnswers: Int, totalQuestions: Int) {
        if (resultSaved.value == true) return

        viewModelScope.launch {
            try {
                getFlagUS.insertFlag(
                    FlagEntity(
                        correctAnswers = correctAnswers,
                        totalQuestions = totalQuestions
                    )
                )
                resultSaved.value = true
            } catch (e: Exception) {
                Log.i("Error", e.message.toString())
            }
        }
    }

    private fun resetResultSaved() {
        resultSaved.value = false
    }
}