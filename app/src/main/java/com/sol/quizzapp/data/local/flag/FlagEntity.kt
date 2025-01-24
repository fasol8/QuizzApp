package com.sol.quizzapp.data.local.flag

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flag_results")
data class FlagEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val date: Long = System.currentTimeMillis()
)