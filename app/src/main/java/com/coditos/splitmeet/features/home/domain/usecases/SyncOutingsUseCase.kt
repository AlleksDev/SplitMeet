package com.coditos.splitmeet.features.home.domain.usecases

import com.coditos.splitmeet.features.home.domain.repositories.HomeRepository
import javax.inject.Inject

class SyncOutingsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        repository.syncOutings()
        return Result.success(Unit)
    }

}