package com.coditos.splitmeet.features.outing.domain.usecases

import com.coditos.splitmeet.features.outing.domain.entities.Outing
import com.coditos.splitmeet.features.outing.domain.repositories.HomeRepository

/**
 * Use case to get all active outings
 */
class GetActiveOutingsUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): NetworkResult<List<Outing>> {
        return repository.getActiveOutings()
    }
}
