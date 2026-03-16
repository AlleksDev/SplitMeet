package com.coditos.splitmeet.features.detailOuting.data.repositories

import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.api.DetailOutingApi
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.mapper.toDomain
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.mapper.toDomainList
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.AddParticipantRequest
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.UpdateOutingRequest
import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingDetail
import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingItem
import com.coditos.splitmeet.features.detailOuting.domain.entities.Participant
import com.coditos.splitmeet.features.detailOuting.domain.entities.SearchUser
import com.coditos.splitmeet.features.detailOuting.domain.repositories.DetailOutingRepository
import com.coditos.splitmeet.features.outing.data.datasources.remote.mapper.toDomainList as toCategoryDomainList
import com.coditos.splitmeet.features.outing.domain.entities.Category
import javax.inject.Inject

class DetailOutingRepositoryImpl @Inject constructor(
    private val api: DetailOutingApi
) : DetailOutingRepository {

    override suspend fun getOutingDetail(outingId: Long): OutingDetail {
        val response = api.getOutingById(outingId)
        return response.toDomain()
    }

    override suspend fun getParticipants(outingId: Long): List<Participant> {
        val response = api.getParticipants(outingId)
        return response.toDomainList()
    }

    override suspend fun getOutingItems(outingId: Long): List<OutingItem> {
        val response = api.getOutingItems(outingId)
        return response.toDomainList()
    }

    override suspend fun searchUsers(username: String): List<SearchUser> {
        val response = api.searchUsers(username)
        return response.toDomainList()
    }

    override suspend fun addParticipant(outingId: Long, userId: Long): Boolean {
        val request = AddParticipantRequest(userId = userId)
        val response = api.addParticipant(outingId, request)
        return response.id != null
    }

    override suspend fun removeParticipant(outingId: Long, userId: Long) {
        api.removeParticipant(outingId, userId)
    }

    override suspend fun confirmPayment(paymentId: Long) {
        api.confirmPayment(paymentId)
    }

    override suspend fun confirmParticipantPayment(outingId: Long, participantId: Long) {
        api.confirmParticipantPayment(outingId, participantId)
    }

    override suspend fun updateOuting(
        outingId: Long,
        name: String,
        description: String?,
        categoryId: Long,
        outingDate: String,
        splitType: String
    ): OutingDetail {
        val request = UpdateOutingRequest(
            name = name,
            description = description,
            categoryId = categoryId,
            outingDate = outingDate,
            splitType = splitType
        )
        val response = api.updateOuting(outingId, request)
        return response.toDomain()
    }

    override suspend fun deleteOuting(outingId: Long) {
        api.deleteOuting(outingId)
    }

    override suspend fun getCategories(): List<Category> {
        val response = api.getCategories()
        return response.toCategoryDomainList()
    }
}
