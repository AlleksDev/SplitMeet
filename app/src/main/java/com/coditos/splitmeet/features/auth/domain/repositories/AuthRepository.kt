package com.coditos.splitmeet.features.auth.domain.repositories

import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.LoginRequest
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.User
import com.coditos.splitmeet.features.auth.domain.entities.LoginResponse
import com.coditos.splitmeet.features.auth.domain.entities.RegisterResponse

interface AuthRepository {
    suspend fun register(user: User): RegisterResponse

    suspend fun login(request: LoginRequest): LoginResponse
}