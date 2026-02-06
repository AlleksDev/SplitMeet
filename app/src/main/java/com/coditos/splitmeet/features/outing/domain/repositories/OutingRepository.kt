package com.coditos.splitmeet.features.outing.domain.repositories

import com.coditos.splitmeet.features.outing.data.datasources.remote.model.CreateOutingRequest
import com.coditos.splitmeet.features.outing.domain.entities.Category
import com.coditos.splitmeet.features.outing.domain.entities.CreatedOuting

interface OutingRepository {
    suspend fun createOuting(request: CreateOutingRequest): CreatedOuting
    suspend fun getCategories(): List<Category>
}
