package com.coditos.splitmeet.features.auth.data.datasoruces.remote.api

import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.LoginRequest
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.LoginResponseDto
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.User
import com.coditos.splitmeet.features.auth.domain.entities.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("users")
    suspend fun register(
        @Body request: User
    ): RegisterResponse

    @POST("users/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponseDto
}
