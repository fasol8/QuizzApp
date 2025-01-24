package com.sol.quizzapp.data.local.wordle

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wordle_results")
data class WordleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gameWon: Boolean,
    val word: String,
    val attempts: Int,
    val difficulty: String,
    val date: Long = System.currentTimeMillis()
)