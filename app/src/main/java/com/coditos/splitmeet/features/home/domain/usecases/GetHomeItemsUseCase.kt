package com.coditos.splitmeet.features.home.domain.usecases

import com.coditos.splitmeet.features.home.domain.entities.Outing
import com.coditos.splitmeet.features.home.domain.repositories.HomeRepository

class GetHomeItemsUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<List<Outing>> {
        return try {
            val response = repository.getOutings()

            if (response.isNotEmpty()) {
                Result.success(response)
            } else {
                Result.failure(Exception("No se econtraron resultados"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
