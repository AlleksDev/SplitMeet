package com.coditos.splitmeet.features.detailOuting.domain.entities

data class OutingDetail(
    val id: Long,
    val name: String,
    val description: String?,
    val categoryId: Long?,
    val categoryName: String?,
    val groupId: Long?,
    val creatorId: Long,
    val outingDate: String,
    val splitType: String,
    val totalAmount: Double,
    val status: String,
    val isEditable: Boolean,
    val participantCount: Int,
    val paidCount: Int
)
