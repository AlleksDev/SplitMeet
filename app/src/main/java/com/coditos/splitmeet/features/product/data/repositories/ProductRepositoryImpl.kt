package com.coditos.splitmeet.features.product.data.repositories

import com.coditos.splitmeet.core.network.SplitMeetApi
import com.coditos.splitmeet.features.product.data.datasources.remote.model.CreateOutingItemRequest
import com.coditos.splitmeet.features.product.data.datasources.remote.model.CreateProductRequest
import com.coditos.splitmeet.features.product.domain.entities.OutingProduct
import com.coditos.splitmeet.features.product.domain.entities.Product
import com.coditos.splitmeet.features.product.domain.repositories.ProductRepository

class ProductRepositoryImpl(
    private val api: SplitMeetApi
) : ProductRepository {

    override suspend fun getProductsByCategory(categoryId: Long): List<Product> {
        val response = api.getProductsByCategory(categoryId)
        return response.map { dto ->
            Product(
                id = dto.id ?: 0,
                categoryId = dto.categoryId,
                name = dto.name ?: "",
                presentation = dto.presentation,
                size = dto.size,
                defaultPrice = dto.defaultPrice,
                isPredefined = dto.isPredefined ?: false
            )
        }
    }

    override suspend fun getOutingProducts(outingId: Long): List<OutingProduct> {
        val response = api.getOutingProducts(outingId)
        return response.map { dto ->
            OutingProduct(
                id = dto.id ?: 0,
                outingId = dto.outingId ?: 0,
                productId = dto.productId,
                productName = dto.productName ?: dto.customName ?: "",
                customName = dto.customName,
                customPresentation = dto.customPresentation,
                presentation = dto.presentation,
                size = dto.size,
                quantity = dto.quantity ?: 1,
                unitPrice = dto.unitPrice ?: 0.0,
                subtotal = dto.subtotal ?: 0.0,
                isShared = dto.isShared ?: false
            )
        }
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
        return OutingProduct(
            id = response.id ?: 0,
            outingId = response.outingId ?: outingId,
            productId = response.productId,
            productName = response.productName ?: response.customName ?: "",
            customName = response.customName,
            customPresentation = response.customPresentation,
            presentation = response.presentation,
            size = response.size,
            quantity = response.quantity ?: quantity,
            unitPrice = response.unitPrice ?: unitPrice,
            subtotal = response.subtotal ?: (quantity * unitPrice),
            isShared = response.isShared ?: isShared
        )
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
        return Product(
            id = response.id ?: 0,
            categoryId = response.categoryId,
            name = response.name ?: name,
            presentation = response.presentation,
            size = response.size,
            defaultPrice = response.defaultPrice,
            isPredefined = response.isPredefined ?: false
        )
    }

    override suspend fun deleteOutingItem(outingId: Long, itemId: Long) {
        api.deleteOutingItem(outingId, itemId)
    }
}
