package com.coditos.splitmeet.features.auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coditos.splitmeet.features.auth.domain.usecases.LoginUseCase
import com.coditos.splitmeet.features.auth.domain.usecases.RegisterUseCase
import com.coditos.splitmeet.features.auth.domain.usecases.SaveTokenUseCase

class AuthViewModelFactory(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val saveTokenUseCase: SaveTokenUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(
                loginUseCase = loginUseCase,
                registerUseCase = registerUseCase,
                saveTokenUseCase = saveTokenUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
