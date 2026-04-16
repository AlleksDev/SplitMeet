package com.coditos.splitmeet.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "outings")
data class OutingEntity (
    @PrimaryKey val id: Long,
    val name: String,
    val description: String?,
    val categoryName: String,
    val splitType: String,
    val totalAmount: Float,
    val participantCount: Int,
    val paidCount: Int,
    val createdAt: String? = null,
    val status: String
)