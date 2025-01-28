package com.sol.quizzapp.data.local.logo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logo_results")
data class LogoEntity(
    @PrimaryKey(autoGenerate = true)val id:Int=0,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val date: Long = System.currentTimeMillis()
)
