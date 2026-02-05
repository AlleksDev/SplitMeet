package com.coditos.splitmeet.features.outing.domain.entities

/**
 * Domain entity representing an Outing (salida)
 */
data class Outing(
    val id: String,
    val title: String,
    val category: String,
    val totalAmount: Double,
    val pendingAmount: Double,
    val totalParticipants: Int,
    val paidParticipants: Int,
    val createdAt: String,
    val isActive: Boolean = true
)

/**
 * Domain entity for creating a new Outing
 */
data class CreateOutingData(
    val title: String,
    val category: String,
    val totalAmount: Double,
    val participantIds: List<String>
)
