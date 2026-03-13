package com.coditos.splitmeet.features.product.domain.usecases

import com.coditos.splitmeet.features.product.domain.entities.Product
import com.coditos.splitmeet.features.product.domain.repositories.ProductRepository
import javax.inject.Inject

class GetProductsByCategoryUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(categoryId: Long): Result<List<Product>> {
        return try {
            val products = repository.getProductsByCategory(categoryId)
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
