package com.coditos.splitmeet.features.outing.data.datasources.dto

import com.coditos.splitmeet.features.outing.domain.entities.Outing
import com.google.gson.annotations.SerializedName

/**
 * DTO for Outing from API
 */
data class OutingDto(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("category")
    val category: String,
    
    @SerializedName("total_amount")
    val totalAmount: Double,
    
    @SerializedName("pending_amount")
    val pendingAmount: Double,
    
    @SerializedName("total_participants")
    val totalParticipants: Int,
    
    @SerializedName("paid_participants")
    val paidParticipants: Int,
    
    @SerializedName("created_at")
    val createdAt: String,
    
    @SerializedName("is_active")
    val isActive: Boolean = true
) {
    fun toDomain(): Outing = Outing(
        id = id,
        title = title,
        category = category,
        totalAmount = totalAmount,
        pendingAmount = pendingAmount,
        totalParticipants = totalParticipants,
        paidParticipants = paidParticipants,
        createdAt = createdAt,
        isActive = isActive
    )
}

/**
 * Extension to map list of DTOs to domain entities
 */
fun List<OutingDto>.toDomainList(): List<Outing> = map { it.toDomain() }

/**
 * Request body for creating an outing
 */
data class CreateOutingRequestDto(
    @SerializedName("title")
    val title: String,
    
    @SerializedName("category")
    val category: String,
    
    @SerializedName("total_amount")
    val totalAmount: Double,
    
    @SerializedName("participant_ids")
    val participantIds: List<String>
)
