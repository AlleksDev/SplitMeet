package com.coditos.splitmeet.features.group.domain.entities

data class GroupMember(
    val id: Long,
    val groupId: Long,
    val userId: Long,
    val role: String,
    val username: String,
    val name: String,
    val email: String
)
