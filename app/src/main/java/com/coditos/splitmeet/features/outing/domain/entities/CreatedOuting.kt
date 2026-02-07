package com.coditos.splitmeet.features.outing.domain.entities

data class CreatedOuting(
    val id: Long,
    val name: String,
    val description: String?,
    val categoryId: Long?,
    val creatorId: Long,
    val outingDate: String,
    val splitType: String,
    val totalAmount: Double,
    val status: String,
    val isEditable: Boolean
)
