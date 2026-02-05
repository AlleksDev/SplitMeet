package com.coditos.splitmeet.features.outing.domain.usecases

import com.coditos.splitmeet.features.outing.domain.entities.CreateOutingData
import com.coditos.splitmeet.features.outing.domain.entities.Outing
import com.coditos.splitmeet.features.outing.domain.repositories.HomeRepository

/**
 * Use case to create a new outing
 */
class CreateOutingUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(
        title: String,
        category: String,
        totalAmount: Double,
        participantIds: List<String>
    ): NetworkResult<Outing> {
        // Business logic validation
        if (title.isBlank()) {
            return NetworkResult.Error("El título es requerido")
        }
        if (category.isBlank()) {
            return NetworkResult.Error("La categoría es requerida")
        }
        if (totalAmount <= 0) {
            return NetworkResult.Error("El monto debe ser mayor a 0")
        }
        if (participantIds.isEmpty()) {
            return NetworkResult.Error("Debe haber al menos un participante")
        }
        
        return repository.createOuting(
            CreateOutingData(title, category, totalAmount, participantIds)
        )
    }
}
