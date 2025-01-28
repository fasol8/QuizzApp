package com.sol.quizzapp.domain.us

import com.sol.quizzapp.data.local.logo.LogoEntity
import com.sol.quizzapp.data.repository.LogoRepository
import javax.inject.Inject

class LogoUS @Inject constructor(private val repository: LogoRepository) {

    suspend fun insertLogo(logo: LogoEntity) {
        return repository.insertResult(logo)
    }
}