package com.coditos.splitmeet.features.auth.data.repositories

import com.coditos.splitmeet.core.network.SplitMeetApi
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.mapper.loginToDomain
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.mapper.registerToDomain
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.LoginRequest
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.LoginResponseDto
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.User
import com.coditos.splitmeet.features.auth.domain.entities.LoginResponse
import com.coditos.splitmeet.features.auth.domain.entities.RegisterResponse
import com.coditos.splitmeet.features.auth.domain.repositories.AuthRepository

class AuthRepositoryImpl(
    private val api: SplitMeetApi
) : AuthRepository {
    override suspend fun register(user: User): RegisterResponse {
        val response = api.register(user)
        return response.registerToDomain()
    }

    override suspend fun login(request: LoginRequest): LoginResponse {
        val response = api.login(request)
        return response.loginToDomain()
    }
}