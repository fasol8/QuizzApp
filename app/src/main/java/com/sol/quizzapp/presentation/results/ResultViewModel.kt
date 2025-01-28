package com.sol.quizzapp.presentation.results

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.quizzapp.data.local.flag.FlagEntity
import com.sol.quizzapp.data.local.logo.LogoEntity
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

    private val _logoResult: MutableStateFlow<List<LogoEntity>> = MutableStateFlow(emptyList())
    val logoResult: StateFlow<List<LogoEntity>> = _logoResult

    fun loadData() {
        getQuizData()
        getFlagData()
        getWordleData()
        getLogoData()
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

    private fun getLogoData() {
        viewModelScope.launch {
            try {
                val response = getResultUS.getAllLogo()
                _logoResult.value = response
            } catch (e: Exception) {
                Log.i("Error", e.message.toString())
            }
        }
    }
}