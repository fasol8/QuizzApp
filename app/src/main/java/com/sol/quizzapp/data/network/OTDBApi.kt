package com.sol.quizzapp.data.network

import com.sol.quizzapp.domain.model.quiz.QuizResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OTDBApi {

    @GET("api.php")
    suspend fun getQuiz(
        @Query("amount") amount: Int,
        @Query("category") category: Int,
        @Query("difficulty") difficulty: String = "easy",
    ): QuizResponse
}