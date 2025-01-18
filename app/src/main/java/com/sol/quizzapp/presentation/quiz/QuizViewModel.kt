package com.sol.quizzapp.presentation.quiz

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.quizzapp.domain.model.quiz.QuizResult
import com.sol.quizzapp.domain.us.QuizUS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(private val getQuizUS: QuizUS) : ViewModel() {

    private val _quiz = MutableLiveData<List<QuizResult>>()
    val quiz: LiveData<List<QuizResult>> = _quiz

    private val _currentQuestionIndex = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int> get() = _currentQuestionIndex

    private val _selectedAnswer = MutableLiveData<String?>(null)
    val selectedAnswer: LiveData<String?> get() = _selectedAnswer

    private val _isNextEnabled = MutableLiveData(false)
    val isNextEnabled: LiveData<Boolean> get() = _isNextEnabled

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    private val _isQuizFinished = MutableLiveData(false)
    val isQuizFinished: LiveData<Boolean> get() = _isQuizFinished

    private val _timerValue = MutableLiveData(30)
    val timerValue: LiveData<Int> get() = _timerValue

    private val _timeExpired = MutableLiveData(false)
    val timeExpired: LiveData<Boolean> get() = _timeExpired

    private var timerJob: Job? = null

    init {
        startTimer()
    }

    fun getLoad(categoryId: Int, difficultSelected: String) {
        getQuiz(10, categoryId, difficultSelected)
    }

    private fun getQuiz(amount: Int, category: Int, difficulty: String) {
        viewModelScope.launch {
            try {
                val response = if (category == 0) getQuizUS.getRandomQuiz(amount) else
                    getQuizUS.getQuiz(amount, category, difficulty)
                _quiz.value = response.results
            } catch (e: Exception) {
                Log.i("Error", e.message.toString())
            }
        }
    }

    fun onAnswerSelected(selectedAnswer: String) {
        if (_timeExpired.value == true) return

        _selectedAnswer.value = selectedAnswer
        val currentIndex = _currentQuestionIndex.value ?: 0
        val currentQuiz = _quiz.value?.get(currentIndex)

        if (currentQuiz != null && selectedAnswer == currentQuiz.correctAnswer) {
            _score.value = (_score.value ?: 0) + 1
        }

        _isNextEnabled.value = true
        timerJob?.cancel()
    }

    fun onNextQuestion() {
        val currentIndex = _currentQuestionIndex.value ?: 0

        if (currentIndex < (_quiz.value?.size ?: 0) - 1) {
            _currentQuestionIndex.value = currentIndex + 1
            _selectedAnswer.value = null
            _isNextEnabled.value = false
            _timeExpired.value = false
            resetTimer()
        } else {
            _isQuizFinished.value = true
        }
    }

    fun decrementTimer() {
        val currentTimer = _timerValue.value ?: 0
        if (currentTimer > 0) {
            _timerValue.value = currentTimer - 1
        } else {
            onTimeExpired()
        }
    }

    private fun resetTimer() {
        _timerValue.value = 30
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while ((_timerValue.value ?: 0) > 0) {
                delay(1000)
                _timerValue.value = (_timerValue.value ?: 1) - 1
            }
            onTimeExpired()
        }
    }

    private fun onTimeExpired() {
        _timeExpired.value = true
        _isNextEnabled.value = true

        if (_selectedAnswer.value == null) {
            val currentIndex = _currentQuestionIndex.value ?: 0
            val currentQuiz = _quiz.value?.get(currentIndex)
            _selectedAnswer.value = currentQuiz?.correctAnswer
        }
    }
}