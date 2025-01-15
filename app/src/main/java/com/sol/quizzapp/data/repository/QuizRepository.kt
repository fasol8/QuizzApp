package com.sol.quizzapp.data.repository

import com.sol.quizzapp.data.network.OTDBApi
import com.sol.quizzapp.domain.model.quiz.QuizResponse
import javax.inject.Inject

class QuizRepository @Inject constructor(private val api: OTDBApi) {

    suspend fun getQuiz(amount: Int, category: Int, difficulty: String): QuizResponse {
        return api.getQuiz(amount, category, difficulty)
    }
}