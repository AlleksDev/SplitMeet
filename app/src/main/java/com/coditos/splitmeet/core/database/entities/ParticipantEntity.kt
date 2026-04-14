package com.coditos.splitmeet.core.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a participant in an outing.
 * This entity is synced with the remote API.
 * 
 * Used for:
 * - Offline-first persistence
 * - UI Optimistic updates (changes reflected immediately in Room before API confirmation)
 * - Automatic rollback if API fails
 */
@Entity(
    tableName = "participants",
    foreignKeys = [
        ForeignKey(
            entity = OutingEntity::class,
            parentColumns = ["id"],
            childColumns = ["outingId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["outingId"], name = "index_participants_outingId")
    ]
)
data class ParticipantEntity(
    @PrimaryKey val id: Long,
    val outingId: Long,
    val userId: Long,
    val username: String,
    val name: String,
    val status: String,  // "pending", "confirmed", "declined"
    val paymentId: Long? = null,
    val paymentStatus: String? = null,  // "pending", "paid", "confirmed"
    val amountOwed: Double = 0.0,
    val customAmount: Double? = null,
    val joinedAt: String? = null
)
