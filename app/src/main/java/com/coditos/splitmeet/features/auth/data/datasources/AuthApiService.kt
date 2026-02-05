package com.coditos.splitmeet.features.auth.data.datasources

import com.coditos.splitmeet.features.auth.data.datasources.dto.LoginRequestDto
import com.coditos.splitmeet.features.auth.data.datasources.dto.RegisterRequestDto
import com.coditos.splitmeet.features.auth.data.datasources.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Retrofit API service for authentication
 */
interface AuthApiService {
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): UserDto
    
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDto): UserDto
    
    @POST("auth/logout")
    suspend fun logout()
    
    @GET("auth/me")
    suspend fun getCurrentUser(): UserDto?
}
