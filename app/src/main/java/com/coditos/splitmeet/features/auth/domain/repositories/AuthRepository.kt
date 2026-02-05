package com.coditos.splitmeet.features.auth.domain.repositories

import com.coditos.splitmeet.features.auth.domain.entities.LoginCredentials
import com.coditos.splitmeet.features.auth.domain.entities.RegisterData
import com.coditos.splitmeet.features.auth.domain.entities.User

/**
 * Repository interface for authentication operations
 */
interface AuthRepository {
    suspend fun login(credentials: LoginCredentials): NetworkResult<User>
    suspend fun register(data: RegisterData): NetworkResult<User>
    suspend fun logout(): NetworkResult<Unit>
    suspend fun getCurrentUser(): NetworkResult<User?>
}
