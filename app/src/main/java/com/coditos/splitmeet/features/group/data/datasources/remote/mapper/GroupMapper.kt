package com.coditos.splitmeet.features.group.data.datasources.remote.mapper

import com.coditos.splitmeet.features.group.data.datasources.remote.model.GroupDto
import com.coditos.splitmeet.features.group.data.datasources.remote.model.GroupMemberDto
import com.coditos.splitmeet.features.group.domain.entities.Group
import com.coditos.splitmeet.features.group.domain.entities.GroupMember

fun GroupDto.toDomain(): Group = Group(
    id = id,
    name = name,
    description = description.orEmpty(),
    ownerId = ownerId,
    memberCount = memberCount ?: 0,
    ownerUsername = ownerUsername.orEmpty()
)

fun GroupMemberDto.toDomain(): GroupMember = GroupMember(
    id = id,
    groupId = groupId,
    userId = userId,
    role = role,
    username = username.orEmpty(),
    name = name.orEmpty(),
    email = email.orEmpty()
)
