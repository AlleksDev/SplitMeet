package com.coditos.splitmeet.features.auth.di

import com.coditos.splitmeet.core.di.AppContainer
import com.coditos.splitmeet.features.auth.domain.usecases.LoginUseCase
import com.coditos.splitmeet.features.auth.domain.usecases.RegisterUseCase
import com.coditos.splitmeet.features.auth.presentation.viewmodels.LoginViewModelFactory
import com.coditos.splitmeet.features.auth.presentation.viewmodels.RegisterViewModelFactory

/**
 * Module for Auth feature (Login & Register) dependency injection
 */
class AuthModule(
    private val appContainer: AppContainer
) {
    
    private fun provideLoginUseCase(): LoginUseCase {
        return LoginUseCase(appContainer.authRepository)
    }
    
    private fun provideRegisterUseCase(): RegisterUseCase {
        return RegisterUseCase(appContainer.authRepository)
    }
    
    fun provideLoginViewModelFactory(): LoginViewModelFactory {
        return LoginViewModelFactory(
            loginUseCase = provideLoginUseCase()
        )
    }
    
    fun provideRegisterViewModelFactory(): RegisterViewModelFactory {
        return RegisterViewModelFactory(
            registerUseCase = provideRegisterUseCase()
        )
    }
}
