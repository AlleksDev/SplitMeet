package com.coditos.splitmeet.features.manageOuting.data.repositories

import com.coditos.splitmeet.features.manageOuting.data.datasources.remote.api.ManageOutingApi
import com.coditos.splitmeet.features.manageOuting.data.datasources.remote.mapper.toDomain
import com.coditos.splitmeet.features.manageOuting.data.datasources.remote.mapper.toDomainList
import com.coditos.splitmeet.features.manageOuting.data.datasources.remote.model.CreateOutingRequest
import com.coditos.splitmeet.features.manageOuting.domain.entities.Category
import com.coditos.splitmeet.features.manageOuting.domain.entities.CreatedOuting
import com.coditos.splitmeet.features.manageOuting.domain.repositories.ManageOutingRepository
import javax.inject.Inject

class ManageOutingRepositoryImpl @Inject constructor(
    private val api: ManageOutingApi
) : ManageOutingRepository {

    override suspend fun createOuting(request: CreateOutingRequest): CreatedOuting {
        val response = api.createOuting(request)
        return response.toDomain()
    }

    override suspend fun getCategories(): List<Category> {
        val response = api.getCategories()
        return response.toDomainList()
    }
}
