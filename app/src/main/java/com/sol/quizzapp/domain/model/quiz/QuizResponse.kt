package com.sol.quizzapp.domain.model.quiz

import com.google.gson.annotations.SerializedName

data class QuizResponse(
    @SerializedName("response_code") val responseCode: Int,
    val results: List<QuizResult>
)