package com.coditos.splitmeet.features.auth.domain.usecases

import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.User
import com.coditos.splitmeet.features.auth.domain.entities.RegisterResponse
import com.coditos.splitmeet.features.auth.domain.repositories.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(user: User): Result<RegisterResponse> {
        return try {
            val response = authRepository.register(user)
            if (response.message.isNotEmpty()) {
                Result.success(response)
            } else {
                Result.failure(Exception("No se pudo registrar el usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}