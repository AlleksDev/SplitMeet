package com.coditos.splitmeet.features.manageOuting.domain.repositories

import com.coditos.splitmeet.features.manageOuting.data.datasources.remote.model.CreateOutingRequest
import com.coditos.splitmeet.features.manageOuting.domain.entities.Category
import com.coditos.splitmeet.features.manageOuting.domain.entities.CreatedOuting

interface ManageOutingRepository {
    suspend fun createOuting(request: CreateOutingRequest): CreatedOuting
    suspend fun getCategories(): List<Category>
}
