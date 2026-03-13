package com.coditos.splitmeet.features.group.domain.usecases

import com.coditos.splitmeet.features.group.domain.repositories.GroupRepository
import javax.inject.Inject

class RemoveMemberUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    suspend operator fun invoke(groupId: Long, userId: Long): Result<Unit> =
        repository.removeMember(groupId, userId)
}
