package com.coditos.splitmeet.features.product.domain.usecases

import com.coditos.splitmeet.features.product.domain.entities.OutingProduct
import com.coditos.splitmeet.features.product.domain.repositories.ProductRepository

class GetOutingProductsUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(outingId: Long): Result<List<OutingProduct>> {
        return try {
            val products = repository.getOutingProducts(outingId)
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
