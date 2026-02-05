package com.coditos.splitmeet.features.outing.domain.usecases

import com.coditos.splitmeet.features.outing.domain.entities.Outing
import com.coditos.splitmeet.features.outing.domain.repositories.HomeRepository

/**
 * Use case to get an outing by ID
 */
class GetOutingByIdUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(id: String): NetworkResult<Outing> {
        if (id.isBlank()) {
            return NetworkResult.Error("El ID del outing es requerido")
        }
        return repository.getOutingById(id)
    }
}
