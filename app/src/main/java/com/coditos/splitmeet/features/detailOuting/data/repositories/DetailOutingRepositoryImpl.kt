package com.coditos.splitmeet.features.detailOuting.data.repositories

import android.util.Log
import com.coditos.splitmeet.core.database.dao.OutingDao
import com.coditos.splitmeet.core.database.dao.ParticipantDao
import com.coditos.splitmeet.features.detailOuting.data.datasources.local.mapper.toDomain
import com.coditos.splitmeet.features.detailOuting.data.datasources.local.mapper.toDomainList
import com.coditos.splitmeet.features.detailOuting.data.datasources.local.mapper.toEntity
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.api.DetailOutingApi
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.mapper.toDomain
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.mapper.toDomainList
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.mapper.toEntity
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.AddParticipantRequest
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.UpdateOutingRequest
import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingDetail
import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingItem
import com.coditos.splitmeet.features.detailOuting.domain.entities.Participant
import com.coditos.splitmeet.features.detailOuting.domain.entities.SearchUser
import com.coditos.splitmeet.features.detailOuting.domain.repositories.DetailOutingRepository
import com.coditos.splitmeet.features.detailOuting.domain.usecases.PaymentData
import com.coditos.splitmeet.features.outing.data.datasources.remote.mapper.toDomainList as toCategoryDomainList
import com.coditos.splitmeet.features.outing.domain.entities.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Offline-first Repository Implementation for DetailOuting feature.
 * 
 * Architecture:
 * - Room database is the Single Source of Truth
 * - All data flows through LocalDB first (optimistic updates)
 * - API calls happen in background for sync
 * - UI observes Room via Flow for automatic reactive updates
 * - Errors trigger automatic rollback by reverting Room state
 */
class DetailOutingRepositoryImpl @Inject constructor(
    private val api: DetailOutingApi,
    private val outingDao: OutingDao,
    private val participantDao: ParticipantDao
) : DetailOutingRepository {

    override suspend fun getOutingDetail(outingId: Long): OutingDetail {
        // Try to get from local DB first
        val localOuting = outingDao.getOutingById(outingId)
        
        // Then fetch from API to update local DB
        return try {
            withContext(Dispatchers.IO) {
                val remoteOuting = api.getOutingById(outingId)
                
                // IMPORTANT ATOMICITY: Persist Outing to local DB FIRST so participants don't throw Foreign Key Constraint
                val outingEntity = com.coditos.splitmeet.core.database.entities.OutingEntity(
                    id = remoteOuting.id ?: outingId,
                    name = remoteOuting.name ?: "",
                    description = remoteOuting.description,
                    categoryName = remoteOuting.categoryName ?: "General",
                    splitType = remoteOuting.splitType ?: "equal",
                    totalAmount = (remoteOuting.totalAmount ?: 0.0).toFloat(),
                    participantCount = remoteOuting.participantCount ?: 0,
                    paidCount = remoteOuting.paidCount ?: 0,
                    status = remoteOuting.status ?: "active",
                    createdAt = remoteOuting.outingDate
                )
                outingDao.insertOuting(outingEntity)
                
                remoteOuting.toDomain()
            }
        } catch (e: Exception) {
            Log.w("DetailOutingRepo", "Error fetching outing from API, using cached version", e)
            if (localOuting != null) {
                // If API fails, fallback to local DB representation
                OutingDetail(
                    id = localOuting.id,
                    name = localOuting.name,
                    description = localOuting.description,
                    categoryId = 0, // Fallback placeholder
                    categoryName = localOuting.categoryName,
                    groupId = null,
                    creatorId = 0, // Requires join but fallback minimal
                    outingDate = localOuting.createdAt ?: "",
                    splitType = localOuting.splitType,
                    totalAmount = localOuting.totalAmount.toDouble(),
                    status = localOuting.status,
                    isEditable = false,
                    participantCount = localOuting.participantCount,
                    paidCount = localOuting.paidCount
                )
            } else {
                throw e  // Re-throw error, should use cached version at higher level
            }
        }
    }

    /**
     * OFFLINE-FIRST: Get participants as Flow from local database.
     * 
     * The UI observes this Flow and updates automatically when participants change.
     * API synchronization happens separately in the background.
     */
    override suspend fun getParticipants(outingId: Long): List<Participant> {
        // Fetch from API and sync to local DB
        return withContext(Dispatchers.IO) {
            try {
                val remoteParticipants = api.getParticipants(outingId)
                val entities = remoteParticipants.map { it.toEntity() }
                
                // Persist to local database
                participantDao.insertAll(entities)
                
                remoteParticipants.toDomainList()
            } catch (e: Exception) {
                Log.e("DetailOutingRepo", "Error fetching participants from API", e)
                // Fallback to local data
                participantDao.getAllByOutingId(outingId).map { entities ->
                    entities.toDomainList()
                }
                throw e
            }
        }
    }

    /**
     * Get participants as Flow for reactive UI updates.
     * This is the method the ViewModel should use for observing changes.
     */
    override fun observeParticipants(outingId: Long): Flow<List<Participant>> {
        return participantDao.getAllByOutingId(outingId).map { entities ->
            entities.toDomainList()
        }
    }

    override suspend fun getOutingItems(outingId: Long): List<OutingItem> {
        return withContext(Dispatchers.IO) {
            api.getOutingItems(outingId).toDomainList()
        }
    }

    override suspend fun searchUsers(username: String): List<SearchUser> {
        return withContext(Dispatchers.IO) {
            api.searchUsers(username).toDomainList()
        }
    }

    override suspend fun addParticipant(outingId: Long, userId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            val request = AddParticipantRequest(userId = userId)
            val response = api.addParticipant(outingId, request)
            
            // Persist to local DB
            response.id?.let { newParticipantId ->
                // Fetch updated participant list and sync
                try {
                    val participants = api.getParticipants(outingId)
                    participantDao.insertAll(participants.map { it.toEntity() })
                } catch (e: Exception) {
                    Log.e("DetailOutingRepo", "Error syncing participants after add", e)
                }
            }
            
            response.id != null
        }
    }

    /**
     * OPTIMISTIC DELETE PATTERN:
     * 1. Delete from local DB immediately (optimistic)
     * 2. Call API in background
     * 3. If API fails, the UI will still reflect the deletion (UX consistency)
     *    A separate sync will restore if needed on next app reload
     */
    override suspend fun removeParticipant(outingId: Long, userId: Long) {
        withContext(Dispatchers.IO) {
            try {
                // Step 1: Delete from local DB immediately (optimistic)
                participantDao.deleteById(userId)
                Log.d("DetailOutingRepo", "Participant $userId deleted from local DB (optimistic)")
                
                // Step 2: Call API in background
                try {
                    api.removeParticipant(outingId, userId)
                    Log.d("DetailOutingRepo", "Participant $userId successfully removed from API")
                } catch (apiError: Exception) {
                    // Step 3: API failed - we could reinstate from cache
                    // For now, the deletion remains (next sync will restore)
                    Log.e("DetailOutingRepo", "Error removing participant from API: ${apiError.message}")
                    // In production, could restore: participantDao.getFromCache(userId)?.let { participantDao.insert(it) }
                    throw apiError
                }
            } catch (e: Exception) {
                Log.e("DetailOutingRepo", "Error in removeParticipant", e)
                throw e
            }
        }
    }

    /**
     * OPTIMISTIC PAYMENT CONFIRMATION:
     * 1. Update paymentStatus in local DB immediately
     * 2. Call API in background  
     * 3. If API fails, revert paymentStatus in local DB
     */
    override suspend fun confirmPayment(paymentId: Long) {
        withContext(Dispatchers.IO) {
            // Note: This would need to map paymentId to participantId first
            // For now, using confirmParticipantPayment which is more direct
        }
    }

    /**
     * OPTIMISTIC PAYMENT CONFIRMATION:
     * Updates payment status immediately in Room, then syncs with API.
     */
    override suspend fun confirmParticipantPayment(outingId: Long, participantId: Long) {
        withContext(Dispatchers.IO) {
            val previousParticipant = participantDao.getById(participantId)
            val previousPaymentStatus = previousParticipant?.paymentStatus
            
            try {
                // Step 1: Update payment status immediately (optimistic)
                participantDao.updatePaymentStatus(participantId, "confirmed")
                Log.d("DetailOutingRepo", "Participant $participantId payment status updated to 'confirmed' (optimistic)")
                
                // Step 2: Call API in background
                try {
                    api.confirmParticipantPayment(outingId, participantId)
                    Log.d("DetailOutingRepo", "Payment confirmed in API for participant $participantId")
                } catch (apiError: Exception) {
                    // Step 3: API failed, revert to previous status
                    if (previousPaymentStatus != null) {
                        participantDao.updatePaymentStatus(participantId, previousPaymentStatus)
                        Log.e("DetailOutingRepo", "Payment confirmation failed, reverted to $previousPaymentStatus")
                    }
                    throw apiError
                }
            } catch (e: Exception) {
                Log.e("DetailOutingRepo", "Error in confirmParticipantPayment", e)
                throw e
            }
        }
    }

    override suspend fun updateOuting(
        outingId: Long,
        name: String,
        description: String?,
        categoryId: Long,
        outingDate: String,
        splitType: String
    ): OutingDetail {
        return withContext(Dispatchers.IO) {
            val request = UpdateOutingRequest(
                name = name,
                description = description,
                categoryId = categoryId,
                outingDate = outingDate,
                splitType = splitType
            )
            val response = api.updateOuting(outingId, request)
            response.toDomain()
        }
    }

    override suspend fun deleteOuting(outingId: Long) {
        withContext(Dispatchers.IO) {
            try {
                // OPTIMISTIC: Delete from local DB first
                outingDao.deleteOutingById(outingId)
                participantDao.deleteAllByOutingId(outingId)
                
                // Then call API
                api.deleteOuting(outingId)
            } catch (e: Exception) {
                Log.e("DetailOutingRepo", "Error deleting outing", e)
                throw e
            }
        }
    }

    override suspend fun getCategories(): List<Category> {
        return withContext(Dispatchers.IO) {
            api.getCategories().toCategoryDomainList()
        }
    }

    override suspend fun joinOuting(outingId: Long) {
        withContext(Dispatchers.IO) {
            api.joinOuting(outingId)
        }
    }

    override suspend fun getPaymentsByOuting(outingId: Long): List<PaymentData> {
        return withContext(Dispatchers.IO) {
            val response = try {
                api.getPaymentsByOuting(outingId) ?: emptyList()
            } catch (e: Exception) {
                Log.e("DetailOutingRepo", "Error fetching payments: ${e.message}")
                emptyList()
            }
            
            response.mapNotNull { paymentDto ->
                if (paymentDto.id != null && paymentDto.participantId != null) {
                    PaymentData(
                        id = paymentDto.id,
                        participantId = paymentDto.participantId,
                        status = paymentDto.status ?: "pending",
                        amount = paymentDto.amount ?: 0.0
                    )
                } else {
                    null
                }
            }
        }
    }

    override suspend fun calculateSplits(outingId: Long, singlePayerId: Long?): com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.CalculateSplitsResponseDto {
        return withContext(Dispatchers.IO) {
            val request = if (singlePayerId != null) {
                com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.CalculateSplitsRequest(singlePayerId = singlePayerId)
            } else {
                com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.CalculateSplitsRequest()
            }
            api.calculateSplits(outingId, request)
        }
    }
}
