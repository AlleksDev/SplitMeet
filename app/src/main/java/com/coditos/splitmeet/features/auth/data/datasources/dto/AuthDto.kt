package com.coditos.splitmeet.features.auth.data.datasources.dto

import com.coditos.splitmeet.features.auth.domain.entities.User
import com.google.gson.annotations.SerializedName

/**
 * DTO for User from API
 */
data class UserDto(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("avatar_url")
    val avatarUrl: String? = null,
    
    @SerializedName("token")
    val token: String? = null
) {
    fun toDomain(): User = User(
        id = id,
        email = email,
        name = name,
        avatarUrl = avatarUrl,
        token = token
    )
}

/**
 * Request body for login
 */
data class LoginRequestDto(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String
)

/**
 * Request body for registration
 */
data class RegisterRequestDto(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String
)
