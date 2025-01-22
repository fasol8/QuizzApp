package com.sol.quizzapp.data.network

import com.sol.quizzapp.domain.model.wordle.WordResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WordApi {

    @GET("word")
    suspend fun getRandomWord(): WordResponse

    @GET("word")
    suspend fun getWordLength(
        @Query("length") length: Int
    ): WordResponse
}