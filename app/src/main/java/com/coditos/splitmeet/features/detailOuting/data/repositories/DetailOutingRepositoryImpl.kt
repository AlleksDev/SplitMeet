package com.coditos.splitmeet.features.detailOuting.data.repositories

import com.coditos.splitmeet.core.network.SplitMeetApi
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.mapper.toDomain
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.mapper.toDomainList
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.AddParticipantRequest
import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingDetail
import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingItem
import com.coditos.splitmeet.features.detailOuting.domain.entities.Participant
import com.coditos.splitmeet.features.detailOuting.domain.entities.SearchUser
import com.coditos.splitmeet.features.detailOuting.domain.repositories.DetailOutingRepository

class DetailOutingRepositoryImpl(
    private val api: SplitMeetApi
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
}
