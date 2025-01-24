package com.sol.quizzapp.data.local.flag

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FlagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(flag: FlagEntity)

    @Query("SELECT * FROM flag_results ORDER BY date DESC")
    suspend fun getAllResults(): List<FlagEntity>
}