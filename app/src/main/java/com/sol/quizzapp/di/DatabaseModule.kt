package com.sol.quizzapp.di

import android.content.Context
import androidx.room.Room
import com.sol.quizzapp.data.local.QuizzesDatabase
import com.sol.quizzapp.data.local.flag.FlagDao
import com.sol.quizzapp.data.local.quiz.QuizDao
import com.sol.quizzapp.data.local.wordle.WordleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): QuizzesDatabase {
        return Room.databaseBuilder(
            appContext,
            QuizzesDatabase::class.java,
            "quizzes_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideQuizDao(database: QuizzesDatabase): QuizDao {
        return database.quizDao()
    }

    @Provides
    fun provideFlagDao(database: QuizzesDatabase): FlagDao {
        return database.flagDao()
    }

    @Provides
    fun provideWordleDao(database: QuizzesDatabase): WordleDao {
        return database.wordleDao()
    }
}