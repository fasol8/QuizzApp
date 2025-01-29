package com.sol.quizzapp.presentation.logo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.quizzapp.data.local.logo.LogoEntity
import com.sol.quizzapp.domain.model.logo.Company
import com.sol.quizzapp.domain.model.logo.QuestionCompany
import com.sol.quizzapp.domain.us.LogoUS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogoViewModel @Inject constructor(private val getLogoUS: LogoUS) : ViewModel() {

    private val _currentQuestion = MutableStateFlow<QuestionCompany?>(null)
    val currentQuestion: StateFlow<QuestionCompany?> = _currentQuestion

    private val _currentRound = MutableStateFlow(1)
    val currentRound: StateFlow<Int> = _currentRound

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _quizFinished = MutableStateFlow(false)
    val quizFinished: StateFlow<Boolean> = _quizFinished

    private val _remainingTime = MutableStateFlow(15)
    val remainingTime: StateFlow<Int> = _remainingTime

    private val _selectedAnswer = MutableStateFlow<Company?>(null)
    val selectedAnswer: StateFlow<Company?> = _selectedAnswer

    private val _nextButtonEnabled = MutableStateFlow(false)
    val nextButtonEnabled: StateFlow<Boolean> = _nextButtonEnabled

    private val _timeExpired = MutableStateFlow(false)
    val timeExpired: StateFlow<Boolean> get() = _timeExpired

    private val totalRounds = 10
    private var resultSaved = MutableStateFlow(false)
    private val usedCompanies = mutableListOf<Company>()
    private var timerJob: Job? = null

    fun selectCategory(category: String, difficult: String) {
        if (category == "Random") {
            loadNextQuestion()
        } else {
            val filteredCompanies = Company.entries.filter { it.category == category }
            if (filteredCompanies.isEmpty()) {
                _quizFinished.value = true
                return
            }
            loadNextQuestion(filteredCompanies)
        }
        resetResultSaved()
    }

    private fun loadNextQuestion(filteredCompanies: List<Company> = Company.entries) {
        if (_currentRound.value > totalRounds) {
            _quizFinished.value = true
        } else {
            _currentQuestion.value = generateQuestion(filteredCompanies)
            _remainingTime.value = 15
            _timeExpired.value = false
            _selectedAnswer.value = null
            _nextButtonEnabled.value = false
        }
    }

    private fun generateQuestion(filteredCompanies: List<Company>): QuestionCompany {
        val availableCompanies = filteredCompanies.filter { it !in usedCompanies }

        if (availableCompanies.isEmpty()) {
            _quizFinished.value = true
            return QuestionCompany(
                Company.GOOGLE,
                emptyList()
            )
        }

        val correctCompany = availableCompanies.random()
        usedCompanies.add(correctCompany)

        val incorrectOptions = (filteredCompanies - correctCompany).shuffled().take(3)
            .ifEmpty {
                Company.entries.filter { it.category != correctCompany.category }.shuffled().take(3)
            }
        val allOptions = (listOf(correctCompany) + incorrectOptions).shuffled()

        return QuestionCompany(correctCompany, allOptions)
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

    fun onAnswerSelected(selectedCompany: Company) {
        if (_timeExpired.value) return

        _selectedAnswer.value = selectedCompany

        if (selectedCompany == _currentQuestion.value?.correctCompany) {
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
        if (resultSaved.value) return

        viewModelScope.launch {
            try {
                getLogoUS.insertLogo(
                    LogoEntity(
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