package com.coditos.splitmeet.features.home.di

import com.coditos.splitmeet.core.di.AppContainer
import com.coditos.splitmeet.features.home.domain.usecases.GetHomeItemByIdUseCase
import com.coditos.splitmeet.features.home.domain.usecases.GetHomeItemsUseCase
import com.coditos.splitmeet.features.home.presentation.viewmodels.HomeViewModelFactory

/**
 * Module for Home feature dependency injection
 */
class HomeModule(
    private val appContainer: AppContainer
) {
    
    private fun provideGetHomeItemsUseCase(): GetHomeItemsUseCase {
        return GetHomeItemsUseCase(appContainer.homeRepository)
    }
    
    private fun provideGetHomeItemByIdUseCase(): GetHomeItemByIdUseCase {
        return GetHomeItemByIdUseCase(appContainer.homeRepository)
    }
    
    fun provideHomeViewModelFactory(): HomeViewModelFactory {
        return HomeViewModelFactory(
            getHomeItemsUseCase = provideGetHomeItemsUseCase(),
            getHomeItemByIdUseCase = provideGetHomeItemByIdUseCase()
        )
    }
}