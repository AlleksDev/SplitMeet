package com.coditos.splitmeet.features.home.domain.usecases

import com.coditos.splitmeet.features.home.domain.entities.Outing
import com.coditos.splitmeet.features.home.domain.repositories.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOutingsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<List<Outing>> {
        return repository.getOutings()
    }
}
