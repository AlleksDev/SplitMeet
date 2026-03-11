package com.coditos.splitmeet.features.group.domain.repositories

import com.coditos.splitmeet.features.group.domain.entities.Group
import com.coditos.splitmeet.features.group.domain.entities.GroupMember

interface GroupRepository {
    suspend fun getMyGroups(): Result<List<Group>>
    suspend fun getGroupById(groupId: Long): Result<Group>
    suspend fun createGroup(name: String, description: String): Result<Group>
    suspend fun updateGroup(groupId: Long, name: String, description: String): Result<Group>
    suspend fun deleteGroup(groupId: Long): Result<Unit>
    suspend fun getMembers(groupId: Long): Result<List<GroupMember>>
    suspend fun inviteMember(groupId: Long, username: String): Result<Unit>
    suspend fun removeMember(groupId: Long, userId: Long): Result<Unit>
}
