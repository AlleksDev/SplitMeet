package com.coditos.splitmeet.features.group.domain.usecases

import com.coditos.splitmeet.features.group.domain.repositories.GroupRepository
import javax.inject.Inject

class InviteMemberUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    suspend operator fun invoke(groupId: Long, username: String): Result<Unit> =
        repository.inviteMember(groupId, username)
}
