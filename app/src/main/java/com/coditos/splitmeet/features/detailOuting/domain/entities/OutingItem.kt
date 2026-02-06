package com.coditos.splitmeet.features.detailOuting.domain.entities

data class OutingItem(
    val id: Long,
    val outingId: Long,
    val productId: Long?,
    val name: String,
    val presentation: String?,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double,
    val isShared: Boolean
)
