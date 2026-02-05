package com.coditos.splitmeet.features.home.domain.usecases

import com.coditos.splitmeet.features.home.domain.repositories.HomeRepository

/**
 * Use case to get a specific home item by ID
 */
class GetHomeItemByIdUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(id: String): NetworkResult<HomeItem> {
        return repository.getHomeItemById(id)
    }
}
