package com.sol.quizzapp.presentation.results

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.quizzapp.data.local.flag.FlagEntity
import com.sol.quizzapp.data.local.quiz.QuizEntity
import com.sol.quizzapp.data.local.wordle.WordleEntity
import com.sol.quizzapp.domain.us.ResultUS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(private val getResultUS: ResultUS) : ViewModel() {

    private val _quizResult: MutableStateFlow<List<QuizEntity>> = MutableStateFlow(emptyList())
    val quizResult: StateFlow<List<QuizEntity>> = _quizResult

    private val _flagResult: MutableStateFlow<List<FlagEntity>> = MutableStateFlow(emptyList())
    val flagResult: StateFlow<List<FlagEntity>> = _flagResult

    private val _wordleResult: MutableStateFlow<List<WordleEntity>> = MutableStateFlow(emptyList())
    val wordleResult: StateFlow<List<WordleEntity>> = _wordleResult

    fun loadData() {
        getQuizData()
        getFlagData()
        getWordleData()
    }

    private fun getQuizData() {
        viewModelScope.launch {
            try {
                val response = getResultUS.getAllQuiz()
                _quizResult.value = response
            } catch (e: Exception) {
                Log.i("Error", e.message.toString())
            }
        }
    }

    private fun getFlagData() {
        viewModelScope.launch {
            try {
                val response = getResultUS.getAllFlag()
                _flagResult.value = response
            } catch (e: Exception) {
                Log.i("Error", e.message.toString())
            }
        }
    }

    private fun getWordleData() {
        viewModelScope.launch {
            try {
                val response = getResultUS.getAllWordle()
                _wordleResult.value = response
            } catch (e: Exception) {
                Log.i("Error", e.message.toString())
            }
        }
    }
}