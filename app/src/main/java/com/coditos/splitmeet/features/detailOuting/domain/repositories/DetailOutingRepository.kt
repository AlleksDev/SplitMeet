package com.coditos.splitmeet.features.detailOuting.domain.repositories

import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingDetail
import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingItem
import com.coditos.splitmeet.features.detailOuting.domain.entities.Participant
import com.coditos.splitmeet.features.detailOuting.domain.entities.SearchUser

interface DetailOutingRepository {
    suspend fun getOutingDetail(outingId: Long): OutingDetail
    suspend fun getParticipants(outingId: Long): List<Participant>
    suspend fun getOutingItems(outingId: Long): List<OutingItem>
    suspend fun searchUsers(username: String): List<SearchUser>
    suspend fun addParticipant(outingId: Long, userId: Long): Boolean
}
