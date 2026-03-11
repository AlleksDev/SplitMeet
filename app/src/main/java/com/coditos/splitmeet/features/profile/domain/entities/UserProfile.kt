package com.coditos.splitmeet.features.profile.domain.entities

data class UserProfile(
    val id: Long,
    val username: String,
    val name: String,
    val email: String,
    val phone: String
)
