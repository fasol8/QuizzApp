package com.sol.quizzapp.domain.us

import com.sol.quizzapp.data.local.flag.FlagEntity
import com.sol.quizzapp.data.local.quiz.QuizEntity
import com.sol.quizzapp.data.local.wordle.WordleEntity
import com.sol.quizzapp.data.repository.ResultRepository
import javax.inject.Inject

class ResultUS @Inject constructor(private val repository: ResultRepository) {

    suspend fun getAllQuiz(): List<QuizEntity> {
        return repository.getAllQuiz()
    }

    suspend fun getAllFlag(): List<FlagEntity> {
        return repository.getAllFlag()
    }

    suspend fun getAllWordle(): List<WordleEntity> {
        return repository.getAllWordle()
    }
}