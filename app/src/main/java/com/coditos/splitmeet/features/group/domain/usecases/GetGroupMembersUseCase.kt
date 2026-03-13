package com.coditos.splitmeet.features.group.domain.usecases

import com.coditos.splitmeet.features.group.domain.entities.GroupMember
import com.coditos.splitmeet.features.group.domain.repositories.GroupRepository
import javax.inject.Inject

class GetGroupMembersUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    suspend operator fun invoke(groupId: Long): Result<List<GroupMember>> =
        repository.getMembers(groupId)
}
