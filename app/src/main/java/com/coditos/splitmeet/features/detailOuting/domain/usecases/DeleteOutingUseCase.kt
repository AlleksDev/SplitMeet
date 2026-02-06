package com.coditos.splitmeet.features.detailOuting.domain.usecases

import com.coditos.splitmeet.features.detailOuting.domain.repositories.DetailOutingRepository

class DeleteOutingUseCase(
    private val repository: DetailOutingRepository
) {
    suspend operator fun invoke(outingId: Long): Result<Unit> {
        return try {
            repository.deleteOuting(outingId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
