package com.coditos.splitmeet.features.detailOuting.domain.usecases

import com.coditos.splitmeet.features.detailOuting.domain.entities.Participant
import com.coditos.splitmeet.features.detailOuting.domain.repositories.DetailOutingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveParticipantsUseCase @Inject constructor(
    private val repository: DetailOutingRepository
) {
    operator fun invoke(outingId: Long): Flow<List<Participant>> {
        return repository.observeParticipants(outingId)
    }
}
