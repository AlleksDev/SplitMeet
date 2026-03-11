package com.coditos.splitmeet.features.outing.domain.usecases

import com.coditos.splitmeet.features.outing.domain.entities.Category
import com.coditos.splitmeet.features.outing.domain.repositories.OutingRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: OutingRepository
) {
    suspend operator fun invoke(): Result<List<Category>> {
        return try {
            val response = repository.getCategories()
            if (response.isNotEmpty()) {
                Result.success(response)
            } else {
                Result.failure(Exception("No se encontraron categorías"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
