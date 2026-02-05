package com.coditos.splitmeet.features.auth.domain.entities

/**
 * Domain entity representing a User
 */
data class User(
    val id: String,
    val email: String,
    val name: String,
    val avatarUrl: String? = null,
    val token: String? = null
)
