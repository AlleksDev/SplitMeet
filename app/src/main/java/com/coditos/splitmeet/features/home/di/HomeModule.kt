package com.coditos.splitmeet.features.home.di

import com.coditos.splitmeet.core.di.AppContainer
import com.coditos.splitmeet.features.home.domain.usecases.GetHomeItemsUseCase
import com.coditos.splitmeet.features.home.presentation.viewmodels.HomeViewModelFactory

class HomeModule(
    private val appContainer: AppContainer
) {

    private fun provideGetHomeItemsUseCase(): GetHomeItemsUseCase {
        return GetHomeItemsUseCase(appContainer.homeRepository)
    }

    fun provideHomeViewModelFactory(): HomeViewModelFactory {
        return HomeViewModelFactory(
            getHomeItemsUseCase = provideGetHomeItemsUseCase()
        )
    }
}
