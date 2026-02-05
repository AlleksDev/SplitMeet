package com.coditos.splitmeet.features.home.domain.usecases

import com.coditos.splitmeet.features.home.domain.repositories.HomeRepository

/**
 * Use case to get all home items
 * Contains the business logic for fetching home items
 */
class GetHomeItemsUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): NetworkResult<List<HomeItem>> {
        return repository.getHomeItems()
    }
}
