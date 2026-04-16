package com.coditos.splitmeet.features.detailOuting.domain.usecases

import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.CalculateSplitsResponseDto
import com.coditos.splitmeet.features.detailOuting.domain.repositories.DetailOutingRepository
import javax.inject.Inject

class CalculateSplitsUseCase @Inject constructor(
    private val repository: DetailOutingRepository
) {
    suspend operator fun invoke(outingId: Long, singlePayerId: Long? = null): Result<Boolean> {
        return try {
            repository.calculateSplits(outingId, singlePayerId)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
