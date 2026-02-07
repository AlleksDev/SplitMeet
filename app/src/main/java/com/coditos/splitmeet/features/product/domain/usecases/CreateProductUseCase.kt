package com.coditos.splitmeet.features.product.domain.usecases

import com.coditos.splitmeet.features.product.domain.entities.Product
import com.coditos.splitmeet.features.product.domain.repositories.ProductRepository

class CreateProductUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        categoryId: Long,
        name: String,
        presentation: String?,
        defaultPrice: Double?
    ): Result<Product> {
        return try {
            val result = repository.createProduct(
                categoryId = categoryId,
                name = name,
                presentation = presentation,
                defaultPrice = defaultPrice
            )
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
