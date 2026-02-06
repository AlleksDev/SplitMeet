package com.coditos.splitmeet.features.product.domain.entities

data class Product(
    val id: Long,
    val categoryId: Long?,
    val name: String,
    val presentation: String?,
    val size: String?,
    val defaultPrice: Double?,
    val isPredefined: Boolean
)
