package com.sol.quizzapp.domain.us

import com.sol.quizzapp.data.repository.WordRepository
import com.sol.quizzapp.domain.model.wordle.WordResponse
import javax.inject.Inject

class WordUS @Inject constructor(private val repository: WordRepository) {

    suspend fun getRandomWord(): WordResponse {
        return repository.getRandomWord()
    }

    suspend fun getWordLength(length: Int): WordResponse {
        return repository.getWordLength(length)
    }
}