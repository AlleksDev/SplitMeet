package com.coditos.splitmeet.features.auth.domain.usecases

data class AuthUseCases(
    val loginUseCase: LoginUseCase,
    val registerUseCase: RegisterUseCase,
    val saveTokenUseCase: SaveTokenUseCase
)
