package com.coditos.splitmeet.features.detailOuting.domain.repositories

import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingDetail
import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingItem
import com.coditos.splitmeet.features.detailOuting.domain.entities.Participant
import com.coditos.splitmeet.features.detailOuting.domain.entities.SearchUser
import com.coditos.splitmeet.features.outing.domain.entities.Category

interface DetailOutingRepository {
    suspend fun getOutingDetail(outingId: Long): OutingDetail
    suspend fun getParticipants(outingId: Long): List<Participant>
    suspend fun getOutingItems(outingId: Long): List<OutingItem>
    suspend fun searchUsers(username: String): List<SearchUser>
    suspend fun addParticipant(outingId: Long, userId: Long): Boolean
    suspend fun removeParticipant(outingId: Long, userId: Long)
    suspend fun confirmPayment(paymentId: Long)
    suspend fun confirmParticipantPayment(outingId: Long, participantId: Long)
    suspend fun updateOuting(
        outingId: Long,
        name: String,
        description: String?,
        categoryId: Long,
        outingDate: String,
        splitType: String
    ): OutingDetail
    suspend fun deleteOuting(outingId: Long)
    suspend fun getCategories(): List<Category>
    suspend fun joinOuting(outingId: Long)
}
