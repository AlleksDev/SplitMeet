package com.coditos.splitmeet.features.auth.di

import com.coditos.splitmeet.features.auth.domain.usecases.AuthUseCases
import com.coditos.splitmeet.features.auth.domain.usecases.LoginUseCase
import com.coditos.splitmeet.features.auth.domain.usecases.RegisterUseCase
import com.coditos.splitmeet.features.auth.domain.usecases.SaveTokenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object AuthUseCaseModule {
    @Provides
    fun provideAuthUseCases(
        loginUseCase: LoginUseCase,
        registerUseCase: RegisterUseCase,
        saveTokenUseCase: SaveTokenUseCase
    ): AuthUseCases = AuthUseCases(
        loginUseCase = loginUseCase,
        registerUseCase = registerUseCase,
        saveTokenUseCase = saveTokenUseCase
    )
}
