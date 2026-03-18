package com.coditos.splitmeet.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.coditos.splitmeet.core.database.entities.ParticipantEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Participant entity.
 * 
 * Optimistic Update Pattern:
 * - deleteParticipant: Deletes immediately from local DB (optimistic)
 * - updateParticipantStatus: Updates status immediately (optimistic)
 * - getAllByOutingId: Exposes Flow for reactive UI updates
 * 
 * The Repository handles API calls and rollback on failure.
 */
@Dao
interface ParticipantDao {
    
    @Query("SELECT * FROM participants WHERE outingId = :outingId ORDER BY name ASC")
    fun getAllByOutingId(outingId: Long): Flow<List<ParticipantEntity>>
    
    @Query("SELECT * FROM participants WHERE id = :id")
    suspend fun getById(id: Long): ParticipantEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(participant: ParticipantEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(participants: List<ParticipantEntity>)
    
    @Update
    suspend fun update(participant: ParticipantEntity)
    
    @Delete
    suspend fun delete(participant: ParticipantEntity)
    
    @Query("DELETE FROM participants WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("DELETE FROM participants WHERE outingId = :outingId")
    suspend fun deleteAllByOutingId(outingId: Long)
    
    /**
     * Update only the payment status of a participant.
     * Used for optimistic UI updates after confirming payment.
     */
    @Query("UPDATE participants SET paymentStatus = :paymentStatus WHERE id = :participantId")
    suspend fun updatePaymentStatus(participantId: Long, paymentStatus: String)
    
    /**
     * Update participant status (confirmed, declined, etc.)
     */
    @Query("UPDATE participants SET status = :status WHERE id = :participantId")
    suspend fun updateStatus(participantId: Long, status: String)
}
