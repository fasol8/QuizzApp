package com.sol.quizzapp.data.repository

import com.sol.quizzapp.data.local.wordle.WordleDao
import com.sol.quizzapp.data.local.wordle.WordleEntity
import com.sol.quizzapp.data.network.WordApi
import com.sol.quizzapp.domain.model.wordle.WordResponse
import javax.inject.Inject

class WordRepository @Inject constructor(
    private val api: WordApi,
    private val wordleDao: WordleDao
) {

    suspend fun getRandomWord(): WordResponse {
        return api.getRandomWord()
    }

    suspend fun getWordLength(length: Int): WordResponse {
        return api.getWordLength(length)
    }

    suspend fun insertResult(wordle: WordleEntity) {
        return wordleDao.insertResult(wordle)
    }
}