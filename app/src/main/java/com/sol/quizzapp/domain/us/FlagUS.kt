package com.sol.quizzapp.domain.us

import com.sol.quizzapp.data.local.flag.FlagEntity
import com.sol.quizzapp.data.repository.FlagRepository
import javax.inject.Inject

class FlagUS @Inject constructor(private val repository: FlagRepository) {

    suspend fun insertFlag(flag: FlagEntity) {
        return repository.insertResult(flag)
    }
}