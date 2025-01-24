package com.sol.quizzapp.data.local.quiz

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val difficulty: String,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val date: Long = System.currentTimeMillis()
)
