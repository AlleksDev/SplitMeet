package com.coditos.splitmeet.features.auth.domain.entities

/**
 * Domain entity for login credentials
 */
data class LoginCredentials(
    val email: String,
    val password: String
)

/**
 * Domain entity for registration data
 */
data class RegisterData(
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)
