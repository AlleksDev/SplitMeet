package com.coditos.splitmeet.features.detailOuting.data.datasources.remote.mapper

import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.OutingItemDto
import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingItem

fun OutingItemDto.toDomain(): OutingItem {
    return OutingItem(
        id = this.id ?: 0,
        outingId = this.outingId ?: 0,
        productId = this.productId,
        name = this.customName ?: this.productName ?: "Producto",
        presentation = this.customPresentation ?: this.presentation,
        quantity = this.quantity ?: 1,
        unitPrice = this.unitPrice ?: 0.0,
        subtotal = this.subtotal ?: ((this.quantity ?: 1) * (this.unitPrice ?: 0.0)),
        isShared = this.isShared ?: false
    )
}

fun List<OutingItemDto>.toDomainList(): List<OutingItem> {
    return this.map { it.toDomain() }
}
