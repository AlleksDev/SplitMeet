package com.coditos.splitmeet.features.product.data.datasources.remote.mapper

import com.coditos.splitmeet.features.product.data.datasources.remote.model.OutingProductDto
import com.coditos.splitmeet.features.product.domain.entities.OutingProduct

fun OutingProductDto.toDomain(): OutingProduct {
    return OutingProduct(
        id = this.id ?: 0,
        outingId = this.outingId ?: 0,
        productId = this.productId,
        productName = this.productName ?: this.customName ?: "",
        customName = this.customName,
        customPresentation = this.customPresentation,
        presentation = this.presentation,
        size = this.size,
        quantity = this.quantity ?: 1,
        unitPrice = this.unitPrice ?: 0.0,
        subtotal = this.subtotal ?: 0.0,
        isShared = this.isShared ?: false
    )
}
