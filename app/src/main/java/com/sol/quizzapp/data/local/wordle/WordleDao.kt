package com.sol.quizzapp.data.local.wordle

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(wordle: WordleEntity)

    @Query("SELECT * FROM wordle_results ORDER BY date DESC ")
    suspend fun getAllResults(): List<WordleEntity>
}