package com.coditos.splitmeet.features.auth.domain.usecases

import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.LoginRequest
import com.coditos.splitmeet.features.auth.domain.entities.LoginResponse
import com.coditos.splitmeet.features.auth.domain.repositories.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = authRepository.login(request)
            if (response.message.isNotEmpty()) {
                Result.success(response)
            } else {
                Result.failure(Exception("No se pudo iniciar sesión"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}