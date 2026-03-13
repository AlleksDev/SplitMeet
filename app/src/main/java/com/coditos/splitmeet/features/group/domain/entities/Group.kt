package com.coditos.splitmeet.features.group.domain.entities

data class Group(
    val id: Long,
    val name: String,
    val description: String,
    val ownerId: Long,
    val memberCount: Int,
    val ownerUsername: String
)
