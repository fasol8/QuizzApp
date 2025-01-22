package com.sol.quizzapp.data.repository

import com.sol.quizzapp.data.network.WordApi
import com.sol.quizzapp.domain.model.wordle.WordResponse
import javax.inject.Inject

class WordRepository @Inject constructor(private val api: WordApi) {

    suspend fun getRandomWord(): WordResponse {
        return api.getRandomWord()
    }

    suspend fun getWordLength(length: Int): WordResponse {
        return api.getWordLength(length)
    }
}