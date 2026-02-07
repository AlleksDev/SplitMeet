package com.coditos.splitmeet.features.outing.domain.usecases

import com.coditos.splitmeet.features.outing.data.datasources.remote.model.CreateOutingRequest
import com.coditos.splitmeet.features.outing.domain.entities.CreatedOuting
import com.coditos.splitmeet.features.outing.domain.repositories.OutingRepository

class CreateOutingUseCase(
    private val repository: OutingRepository
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
