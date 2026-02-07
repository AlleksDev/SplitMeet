package com.coditos.splitmeet.features.detailOuting.domain.usecases

import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingDetail
import com.coditos.splitmeet.features.detailOuting.domain.repositories.DetailOutingRepository

class UpdateOutingUseCase(
    private val repository: DetailOutingRepository
) {
    suspend operator fun invoke(
        outingId: Long,
        name: String,
        description: String?,
        categoryId: Long,
        outingDate: String,
        splitType: String
    ): Result<OutingDetail> {
        return try {
            val result = repository.updateOuting(
                outingId = outingId,
                name = name,
                description = description,
                categoryId = categoryId,
                outingDate = outingDate,
                splitType = splitType
            )
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
