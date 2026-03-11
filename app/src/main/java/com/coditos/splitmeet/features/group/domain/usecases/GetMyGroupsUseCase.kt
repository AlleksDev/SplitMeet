package com.coditos.splitmeet.features.group.domain.usecases

import com.coditos.splitmeet.features.group.domain.entities.Group
import com.coditos.splitmeet.features.group.domain.repositories.GroupRepository
import javax.inject.Inject

class GetMyGroupsUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    suspend operator fun invoke(): Result<List<Group>> = repository.getMyGroups()
}
