package com.coditos.splitmeet.features.manageOuting.domain.usecases

import com.coditos.splitmeet.features.manageOuting.domain.entities.Category
import com.coditos.splitmeet.features.manageOuting.domain.repositories.ManageOutingRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: ManageOutingRepository
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
