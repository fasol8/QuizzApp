package com.sol.quizzapp.data.repository

import com.sol.quizzapp.data.local.flag.FlagDao
import com.sol.quizzapp.data.local.flag.FlagEntity
import com.sol.quizzapp.data.local.quiz.QuizDao
import com.sol.quizzapp.data.local.quiz.QuizEntity
import com.sol.quizzapp.data.local.wordle.WordleDao
import com.sol.quizzapp.data.local.wordle.WordleEntity
import javax.inject.Inject

class ResultRepository @Inject constructor(
    private val quizDao: QuizDao,
    private val flagDao: FlagDao,
    private val wordleDao: WordleDao
) {

    suspend fun getAllQuiz(): List<QuizEntity> {
        return quizDao.getAllResults()
    }

    suspend fun getAllFlag(): List<FlagEntity> {
        return flagDao.getAllResults()
    }

    suspend fun getAllWordle(): List<WordleEntity> {
        return wordleDao.getAllResults()
    }
}