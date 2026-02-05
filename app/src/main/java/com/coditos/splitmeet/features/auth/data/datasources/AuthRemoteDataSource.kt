package com.coditos.splitmeet.features.auth.data.datasources

import com.coditos.splitmeet.core.network.safeApiCall
import com.coditos.splitmeet.features.auth.data.datasources.dto.LoginRequestDto
import com.coditos.splitmeet.features.auth.data.datasources.dto.RegisterRequestDto
import com.coditos.splitmeet.features.auth.data.datasources.dto.UserDto

/**
 * Remote data source for authentication
 */
class AuthRemoteDataSource(
    private val apiService: AuthApiService
) {
    
    suspend fun login(email: String, password: String): NetworkResult<UserDto> {
        return safeApiCall { 
            apiService.login(LoginRequestDto(email, password)) 
        }
    }
    
    suspend fun register(name: String, email: String, password: String): NetworkResult<UserDto> {
        return safeApiCall { 
            apiService.register(RegisterRequestDto(name, email, password)) 
        }
    }
    
    suspend fun logout(): NetworkResult<Unit> {
        return safeApiCall { apiService.logout() }
    }
    
    suspend fun getCurrentUser(): NetworkResult<UserDto?> {
        return safeApiCall { apiService.getCurrentUser() }
    }
}
