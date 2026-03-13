package com.coditos.splitmeet.features.home.domain.usecases

import com.coditos.splitmeet.features.home.domain.entities.Outing
import com.coditos.splitmeet.features.home.domain.repositories.HomeRepository
import javax.inject.Inject

class GetHomeItemsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<List<Outing>> {
        return try {
            val response = repository.getOutings()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
