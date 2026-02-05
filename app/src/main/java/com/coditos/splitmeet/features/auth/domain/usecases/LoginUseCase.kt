package com.coditos.splitmeet.features.auth.domain.usecases

import com.coditos.splitmeet.features.auth.domain.entities.LoginCredentials
import com.coditos.splitmeet.features.auth.domain.entities.User
import com.coditos.splitmeet.features.auth.domain.repositories.AuthRepository

/**
 * Use case for user login
 */
class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): NetworkResult<User> {
        // Business logic validation
        if (email.isBlank()) {
            return NetworkResult.Error("El email es requerido")
        }
        if (password.isBlank()) {
            return NetworkResult.Error("La contraseña es requerida")
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return NetworkResult.Error("El email no es válido")
        }
        
        return repository.login(LoginCredentials(email, password))
    }
}
