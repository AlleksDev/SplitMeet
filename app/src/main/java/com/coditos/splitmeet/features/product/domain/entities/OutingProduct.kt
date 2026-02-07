package com.coditos.splitmeet.features.product.domain.entities

data class OutingProduct(
    val id: Long,
    val outingId: Long,
    val productId: Long?,
    val productName: String,
    val customName: String?,
    val customPresentation: String?,
    val presentation: String?,
    val size: String?,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double,
    val isShared: Boolean
)
