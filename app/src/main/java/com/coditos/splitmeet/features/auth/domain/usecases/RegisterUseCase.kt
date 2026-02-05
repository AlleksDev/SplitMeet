package com.coditos.splitmeet.features.auth.domain.usecases

import com.coditos.splitmeet.features.auth.domain.entities.RegisterData
import com.coditos.splitmeet.features.auth.domain.entities.User
import com.coditos.splitmeet.features.auth.domain.repositories.AuthRepository

/**
 * Use case for user registration
 */
class RegisterUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): NetworkResult<User> {
        // Business logic validation
        if (name.isBlank()) {
            return NetworkResult.Error("El nombre es requerido")
        }
        if (email.isBlank()) {
            return NetworkResult.Error("El email es requerido")
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return NetworkResult.Error("El email no es válido")
        }
        if (password.isBlank()) {
            return NetworkResult.Error("La contraseña es requerida")
        }
        if (password.length < 6) {
            return NetworkResult.Error("La contraseña debe tener al menos 6 caracteres")
        }
        if (password != confirmPassword) {
            return NetworkResult.Error("Las contraseñas no coinciden")
        }
        
        return repository.register(RegisterData(name, email, password, confirmPassword))
    }
}
