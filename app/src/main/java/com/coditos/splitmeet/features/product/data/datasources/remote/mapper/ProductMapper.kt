package com.coditos.splitmeet.features.product.data.datasources.remote.mapper

import com.coditos.splitmeet.features.product.data.datasources.remote.model.ProductDto
import com.coditos.splitmeet.features.product.domain.entities.Product

fun ProductDto.toDomain(): Product {
    return Product(
        id = this.id ?: 0,
        categoryId = this.categoryId,
        name = this.name ?: "",
        presentation = this.presentation,
        size = this.size,
        defaultPrice = this.defaultPrice,
        isPredefined = this.isPredefined ?: false
    )
}
