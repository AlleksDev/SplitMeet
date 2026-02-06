package com.coditos.splitmeet.features.home.domain.usecases

import com.coditos.splitmeet.features.home.domain.entities.Outing
import com.coditos.splitmeet.features.home.domain.repositories.HomeRepository

class GetHomeItemByIdUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(id: String): Outing {
        return repository.getOutingById(id)
    }
}
