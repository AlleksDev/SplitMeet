package com.coditos.splitmeet.features.outing.data.repositories

import com.coditos.splitmeet.features.outing.data.datasources.remote.api.OutingApi
import com.coditos.splitmeet.features.outing.data.datasources.remote.mapper.toDomain
import com.coditos.splitmeet.features.outing.data.datasources.remote.mapper.toDomainList
import com.coditos.splitmeet.features.outing.data.datasources.remote.model.CreateOutingRequest
import com.coditos.splitmeet.features.outing.domain.entities.Category
import com.coditos.splitmeet.features.outing.domain.entities.CreatedOuting
import com.coditos.splitmeet.features.outing.domain.repositories.OutingRepository
import javax.inject.Inject

class OutingRepositoryImpl @Inject constructor(
    private val api: OutingApi
) : OutingRepository {

    override suspend fun createOuting(request: CreateOutingRequest): CreatedOuting {
        val response = api.createOuting(request)
        return response.toDomain()
    }

    override suspend fun getCategories(): List<Category> {
        val response = api.getCategories()
        return response.toDomainList()
    }
}
