package com.sol.quizzapp.di

import com.sol.quizzapp.data.network.OTDBApi
import com.sol.quizzapp.data.network.OTDBRetrofit
import com.sol.quizzapp.data.network.WordApi
import com.sol.quizzapp.data.network.WordRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @OTDBRetrofit
    fun provideOTDBRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://opentdb.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @WordRetrofit
    fun provideWordRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://random-word-api.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOTDBApi(@OTDBRetrofit retrofit: Retrofit): OTDBApi {
        return retrofit.create(OTDBApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWordApi(@WordRetrofit retrofit: Retrofit): WordApi {
        return retrofit.create(WordApi::class.java)
    }
}