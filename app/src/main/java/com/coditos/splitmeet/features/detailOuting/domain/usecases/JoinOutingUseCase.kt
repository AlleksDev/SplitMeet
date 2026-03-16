package com.coditos.splitmeet.features.detailOuting.domain.usecases

import com.coditos.splitmeet.features.detailOuting.domain.repositories.DetailOutingRepository
import javax.inject.Inject

class JoinOutingUseCase @Inject constructor(
    private val repository: DetailOutingRepository
) {
    suspend operator fun invoke(outingId: Long): Result<Unit> = try {
        repository.joinOuting(outingId)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
