package com.sol.quizzapp.presentation.logo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.quizzapp.data.local.logo.LogoEntity
import com.sol.quizzapp.domain.model.logo.Company
import com.sol.quizzapp.domain.model.logo.QuestionCompany
import com.sol.quizzapp.domain.model.util.DifficultMode
import com.sol.quizzapp.domain.us.LogoUS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogoViewModel @Inject constructor(private val getLogoUS: LogoUS) : ViewModel() {

    private val _gameMode = MutableStateFlow(DifficultMode.EASY)
    val gameMode: StateFlow<DifficultMode> = _gameMode

    private val _totalRounds = MutableStateFlow(6)
    val totalRounds: StateFlow<Int> = _totalRounds

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

    private val _userInput = MutableStateFlow("")
    val userInput: StateFlow<String> = _userInput

    private val _attemptsLeft = MutableStateFlow(3)
    val attemptsLeft: StateFlow<Int> = _attemptsLeft

    private val _hint = MutableStateFlow("")
    val hint: StateFlow<String> = _hint

    private val selectedCategory = MutableStateFlow<String?>(null)
    private var resultSaved = MutableStateFlow(false)
    private val usedCompanies = mutableListOf<Company>()
    private var timerJob: Job? = null

    fun selectCategory(category: String, difficult: String) {
        selectedCategory.value = if (category == "Random") null else category

        val mode = when (difficult) {
            "hard" -> DifficultMode.HARD
            "medium" -> DifficultMode.MEDIUM
            else -> DifficultMode.EASY
        }
        selectGameMode(mode)
        resetGame()
        resetResultSaved()
    }

    private fun selectGameMode(mode: DifficultMode) {
        _gameMode.value = mode
        _totalRounds.value = when (mode) {
            DifficultMode.EASY -> 6
            DifficultMode.MEDIUM -> 9
            DifficultMode.HARD -> 4
        }
    }

    private fun resetGame() {
        usedCompanies.clear()
        _currentRound.value = 1
        _score.value = 0
        loadNextQuestion()
    }

    private fun loadNextQuestion() {
        if (_currentRound.value > _totalRounds.value) {
            _quizFinished.value = true
        } else {
            _currentQuestion.value = generateQuestion()
            _userInput.value = ""
            _attemptsLeft.value = 3
            _hint.value = ""
            _remainingTime.value = 15
            _timeExpired.value = false
            _selectedAnswer.value = null
            _nextButtonEnabled.value = false
        }
    }

    private fun generateQuestion(): QuestionCompany {
        val selectedCategory = selectedCategory.value

        val availableCompanies = if (selectedCategory != null) {
            Company.entries.filter { it.category == selectedCategory && it !in usedCompanies }
        } else {
            Company.entries.filter { it !in usedCompanies }
        }

        if (availableCompanies.isEmpty()) {
            _quizFinished.value = true
            return QuestionCompany(Company.entries.random(), emptyList()) // Fallback
        }

        val correctCompany = availableCompanies.random()
        usedCompanies.add(correctCompany)

        return when (_gameMode.value) {
            DifficultMode.EASY, DifficultMode.MEDIUM -> {
                val incorrectOptions = availableCompanies.shuffled().take(3)
                val allOptions = (listOf(correctCompany) + incorrectOptions).shuffled()
                QuestionCompany(correctCompany, allOptions)
            }

            DifficultMode.HARD -> {
                QuestionCompany(correctCompany, emptyList())
            }
        }
    }

    fun onAnswerSelected(selectedCompany: Company) {
        if (_gameMode.value == DifficultMode.HARD) return
        if (_timeExpired.value) return

        _selectedAnswer.value = selectedCompany

        if (selectedCompany == _currentQuestion.value?.correctCompany) {
            _score.value += 1
        }

        _nextButtonEnabled.value = true
        timerJob?.cancel()
    }

    fun onUserInputChanged(input: String) {
        if (_gameMode.value == DifficultMode.HARD) {
            _userInput.value = input
        }
    }

    fun checkAnswer() {
        if (_gameMode.value != DifficultMode.HARD) return

        val correctName = _currentQuestion.value?.correctCompany?.name ?: return

        if (_userInput.value.equals(correctName, ignoreCase = true)) {
            _score.value += 1
            _nextButtonEnabled.value = true

            viewModelScope.launch {
                delay(500)
                onNextClicked()
            }
        } else {
            _attemptsLeft.value -= 1
            updateHint()
            if (_attemptsLeft.value == 0) {
                _nextButtonEnabled.value = true

                viewModelScope.launch {
                    delay(1000)
                    onNextClicked()
                }
            }
        }
    }

    private fun updateHint() {
        val correctName = _currentQuestion.value?.correctCompany?.name ?: return
        when (_attemptsLeft.value) {
            2 -> _hint.value =
                "Tiene ${correctName.length} letras y ${correctName.split(" ").size} palabras."

            1 -> _hint.value =
                "Empieza con '${correctName.first()}' y termina con '${correctName.last()}'."

            0 -> {
                _hint.value = "La respuesta correcta era: $correctName"
                _nextButtonEnabled.value = true
            }
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

    fun onNextClicked() {
        if (!_nextButtonEnabled.value) return

        _currentRound.value += 1
        _nextButtonEnabled.value = false
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