package com.sol.quizzapp.data.repository

import com.sol.quizzapp.data.local.flag.FlagDao
import com.sol.quizzapp.data.local.flag.FlagEntity
import javax.inject.Inject

class FlagRepository @Inject constructor(private val flagDao: FlagDao) {

    suspend fun insertResult(flag: FlagEntity){
        return flagDao.insertResult(flag)
    }
}