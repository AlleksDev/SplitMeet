package com.coditos.splitmeet.features.manageOuting.domain.usecases

import com.coditos.splitmeet.features.manageOuting.data.datasources.remote.model.CreateOutingRequest
import com.coditos.splitmeet.features.manageOuting.domain.entities.CreatedOuting
import com.coditos.splitmeet.features.manageOuting.domain.repositories.ManageOutingRepository
import javax.inject.Inject

class CreateOutingUseCase @Inject constructor(
    private val repository: ManageOutingRepository
) {
    suspend operator fun invoke(request: CreateOutingRequest): Result<CreatedOuting> {
        return try {
            val response = repository.createOuting(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
