package com.coditos.splitmeet.features.detailOuting.domain.usecases

import com.coditos.splitmeet.features.detailOuting.domain.repositories.DetailOutingRepository

class AddParticipantUseCase(
    private val repository: DetailOutingRepository
) {
    suspend operator fun invoke(outingId: Long, userId: Long): Result<Boolean> {
        return try {
            val success = repository.addParticipant(outingId, userId)
            Result.success(success)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
