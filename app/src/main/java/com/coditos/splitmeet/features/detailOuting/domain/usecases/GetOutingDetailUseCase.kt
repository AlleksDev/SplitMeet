package com.coditos.splitmeet.features.detailOuting.domain.usecases

import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingDetail
import com.coditos.splitmeet.features.detailOuting.domain.repositories.DetailOutingRepository

class GetOutingDetailUseCase(
    private val repository: DetailOutingRepository
) {
    suspend operator fun invoke(outingId: Long): Result<OutingDetail> {
        return try {
            val detail = repository.getOutingDetail(outingId)
            Result.success(detail)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
