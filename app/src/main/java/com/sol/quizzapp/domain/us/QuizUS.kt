package com.sol.quizzapp.domain.us

import com.sol.quizzapp.data.local.quiz.QuizEntity
import com.sol.quizzapp.data.repository.QuizRepository
import com.sol.quizzapp.domain.model.quiz.QuizResponse
import javax.inject.Inject

class QuizUS @Inject constructor(private val repository: QuizRepository) {

    suspend fun getQuiz(amount: Int, category: Int, difficulty: String): QuizResponse {
        return repository.getQuiz(amount, category, difficulty)
    }

    suspend fun getRandomQuiz(amount: Int): QuizResponse {
        return repository.getRandomQuiz(amount)
    }

    suspend fun insertQuiz(quiz: QuizEntity) {
        return repository.insertResult(quiz)
    }
}