package com.sol.quizzapp.data.repository

import com.sol.quizzapp.data.local.quiz.QuizDao
import com.sol.quizzapp.data.local.quiz.QuizEntity
import com.sol.quizzapp.data.network.OTDBApi
import com.sol.quizzapp.domain.model.quiz.QuizResponse
import javax.inject.Inject

class QuizRepository @Inject constructor(private val api: OTDBApi, private val quizDao: QuizDao) {

    suspend fun getQuiz(amount: Int, category: Int, difficulty: String): QuizResponse {
        return api.getQuiz(amount, category, difficulty)
    }

    suspend fun getRandomQuiz(amount: Int): QuizResponse {
        return api.getRandomQuiz(amount)
    }

    suspend fun insertResult(quiz: QuizEntity) {
        return quizDao.insertResult(quiz)
    }
}