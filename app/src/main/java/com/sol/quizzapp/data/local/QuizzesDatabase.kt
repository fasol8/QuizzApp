package com.sol.quizzapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sol.quizzapp.data.local.flag.FlagDao
import com.sol.quizzapp.data.local.flag.FlagEntity
import com.sol.quizzapp.data.local.logo.LogoDao
import com.sol.quizzapp.data.local.logo.LogoEntity
import com.sol.quizzapp.data.local.quiz.QuizDao
import com.sol.quizzapp.data.local.quiz.QuizEntity
import com.sol.quizzapp.data.local.wordle.WordleDao
import com.sol.quizzapp.data.local.wordle.WordleEntity

@Database(
    entities = [QuizEntity::class, FlagEntity::class, WordleEntity::class, LogoEntity::class],
    version = 3
)
abstract class QuizzesDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
    abstract fun flagDao(): FlagDao
    abstract fun wordleDao(): WordleDao
    abstract fun logoDao(): LogoDao
}
