package com.coditos.splitmeet.features.auth.di

import com.coditos.splitmeet.core.di.AppContainer
import com.coditos.splitmeet.features.auth.domain.usecases.LoginUseCase
import com.coditos.splitmeet.features.auth.domain.usecases.RegisterUseCase
import com.coditos.splitmeet.features.auth.domain.usecases.SaveTokenUseCase
import com.coditos.splitmeet.features.auth.presentation.viewmodels.AuthViewModelFactory

class AuthModule(
    private val appContainer: AppContainer
) {
    private fun provideLoginUseCase(): LoginUseCase {
        return LoginUseCase(appContainer.authRepository)
    }

    private fun provideRegisterUseCase(): RegisterUseCase {
        return RegisterUseCase(appContainer.authRepository)
    }

    private fun provideSaveTokenUseCase(): SaveTokenUseCase {
        return SaveTokenUseCase(appContainer.tokenDataStore)
    }

    fun provideAuthViewModelFactory(): AuthViewModelFactory {
        return AuthViewModelFactory(
            loginUseCase = provideLoginUseCase(),
            registerUseCase = provideRegisterUseCase(),
            saveTokenUseCase = provideSaveTokenUseCase()
        )
    }
}