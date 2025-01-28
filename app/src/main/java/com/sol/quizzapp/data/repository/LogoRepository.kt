package com.sol.quizzapp.data.repository

import com.sol.quizzapp.data.local.logo.LogoDao
import com.sol.quizzapp.data.local.logo.LogoEntity
import javax.inject.Inject

class LogoRepository @Inject constructor(private val logoDao: LogoDao) {

    suspend fun insertResult(logo: LogoEntity) {
        return logoDao.insertResult(logo)
    }
}