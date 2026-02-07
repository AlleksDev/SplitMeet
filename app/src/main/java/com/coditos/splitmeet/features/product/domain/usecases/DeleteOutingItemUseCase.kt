package com.coditos.splitmeet.features.product.domain.usecases

import com.coditos.splitmeet.features.product.domain.repositories.ProductRepository

class DeleteOutingItemUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(outingId: Long, itemId: Long): Result<Unit> {
        return try {
            repository.deleteOutingItem(outingId, itemId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
