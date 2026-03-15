package com.coditos.splitmeet.features.profile.domain.usecases

data class ProfileUseCases(
    val getProfile: GetProfileUseCase,
    val logout: LogoutUseCase,
    val updateProfile: UpdateProfileUseCase
)
