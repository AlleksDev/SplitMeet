package com.coditos.splitmeet.features.detailOuting.domain.usecases

import com.coditos.splitmeet.features.detailOuting.domain.entities.Participant
import com.coditos.splitmeet.features.detailOuting.domain.repositories.DetailOutingRepository

class GetParticipantsUseCase(
    private val repository: DetailOutingRepository
) {
    suspend operator fun invoke(outingId: Long): Result<List<Participant>> {
        return try {
            val participants = repository.getParticipants(outingId)
            Result.success(participants)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
