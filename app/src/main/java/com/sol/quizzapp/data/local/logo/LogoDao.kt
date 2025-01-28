package com.sol.quizzapp.data.local.logo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LogoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(logo: LogoEntity)

    @Query("SELECT * FROM logo_results ORDER BY date DESC")
    suspend fun getAllResults(): List<LogoEntity>
}