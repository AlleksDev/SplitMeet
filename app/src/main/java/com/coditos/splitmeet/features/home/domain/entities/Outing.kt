package com.coditos.splitmeet.features.home.domain.entities

data class Outing(
    val id: Long,
    val name: String,
    val description: String,
    val categoryName: String,
    val splitType: String,
    val totalAmount: Float,
    val participantCount: Int,
    val paidCount: Int
)