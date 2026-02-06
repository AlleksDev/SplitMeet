package com.coditos.splitmeet.features.detailOuting.domain.usecases

import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingItem
import com.coditos.splitmeet.features.detailOuting.domain.repositories.DetailOutingRepository

class GetOutingItemsUseCase(
    private val repository: DetailOutingRepository
) {
    suspend operator fun invoke(outingId: Long): Result<List<OutingItem>> {
        return try {
            val items = repository.getOutingItems(outingId)
            Result.success(items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
