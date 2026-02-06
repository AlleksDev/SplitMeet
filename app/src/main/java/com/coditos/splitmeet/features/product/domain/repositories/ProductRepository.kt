package com.coditos.splitmeet.features.product.domain.repositories

import com.coditos.splitmeet.features.product.domain.entities.OutingProduct
import com.coditos.splitmeet.features.product.domain.entities.Product

interface ProductRepository {
    suspend fun getProductsByCategory(categoryId: Long): List<Product>
    suspend fun getOutingProducts(outingId: Long): List<OutingProduct>
    suspend fun addOutingItem(
        outingId: Long,
        productId: Long?,
        customName: String?,
        customPresentation: String?,
        quantity: Int,
        unitPrice: Double,
        isShared: Boolean
    ): OutingProduct
    suspend fun createProduct(
        categoryId: Long,
        name: String,
        presentation: String?,
        defaultPrice: Double?
    ): Product
    suspend fun deleteOutingItem(outingId: Long, itemId: Long)
}
