package com.sol.quizzapp.data.local.quiz

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(quiz: QuizEntity)

    @Query("SELECT * FROM quiz_results ORDER BY date DESC")
    suspend fun getAllResults(): List<QuizEntity>
}