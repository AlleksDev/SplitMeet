package com.coditos.splitmeet.features.product.domain.usecases

import com.coditos.splitmeet.features.product.domain.entities.OutingProduct
import com.coditos.splitmeet.features.product.domain.repositories.ProductRepository

class AddOutingItemUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        outingId: Long,
        productId: Long?,
        customName: String?,
        customPresentation: String?,
        quantity: Int,
        unitPrice: Double,
        isShared: Boolean
    ): Result<OutingProduct> {
        return try {
            val result = repository.addOutingItem(
                outingId = outingId,
                productId = productId,
                customName = customName,
                customPresentation = customPresentation,
                quantity = quantity,
                unitPrice = unitPrice,
                isShared = isShared
            )
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
