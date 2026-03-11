package com.coditos.splitmeet.features.product.data.repositories

import com.coditos.splitmeet.features.product.data.datasources.remote.api.ProductApi
import com.coditos.splitmeet.features.product.data.datasources.remote.mapper.toDomain
import com.coditos.splitmeet.features.product.data.datasources.remote.model.CreateOutingItemRequest
import com.coditos.splitmeet.features.product.data.datasources.remote.model.CreateProductRequest
import com.coditos.splitmeet.features.product.domain.entities.OutingProduct
import com.coditos.splitmeet.features.product.domain.entities.Product
import com.coditos.splitmeet.features.product.domain.repositories.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi
) : ProductRepository {

    override suspend fun getProductsByCategory(categoryId: Long): List<Product> {
        val response = api.getProductsByCategory(categoryId)
        return response.map { it.toDomain() }
    }

    override suspend fun getOutingProducts(outingId: Long): List<OutingProduct> {
        val response = api.getOutingProducts(outingId)
        return response.map { it.toDomain() }
    }

    override suspend fun addOutingItem(
        outingId: Long,
        productId: Long?,
        customName: String?,
        customPresentation: String?,
        quantity: Int,
        unitPrice: Double,
        isShared: Boolean
    ): OutingProduct {
        val request = CreateOutingItemRequest(
            productId = productId,
            customName = customName,
            customPresentation = customPresentation,
            quantity = quantity,
            unitPrice = unitPrice,
            isShared = isShared
        )
        val response = api.addOutingItem(outingId, request)
        return response.toDomain()
    }

    override suspend fun createProduct(
        categoryId: Long,
        name: String,
        presentation: String?,
        defaultPrice: Double?
    ): Product {
        val request = CreateProductRequest(
            categoryId = categoryId,
            name = name,
            presentation = presentation,
            defaultPrice = defaultPrice
        )
        val response = api.createProduct(request)
        return response.toDomain()
    }

    override suspend fun deleteOutingItem(outingId: Long, itemId: Long) {
        api.deleteOutingItem(outingId, itemId)
    }
}
