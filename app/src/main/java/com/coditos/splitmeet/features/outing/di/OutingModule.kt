package com.coditos.splitmeet.features.outing.di

import com.coditos.splitmeet.core.di.AppContainer
import com.coditos.splitmeet.features.outing.domain.usecases.CreateOutingUseCase
import com.coditos.splitmeet.features.outing.domain.usecases.GetCategoriesUseCase
import com.coditos.splitmeet.features.outing.presentation.viewmodels.OutingViewModelFactory

class OutingModule(
    private val appContainer: AppContainer
) {
    private fun provideCreateOutingUseCase(): CreateOutingUseCase {
        return CreateOutingUseCase(appContainer.outingRepository)
    }

    private fun provideGetCategoriesUseCase(): GetCategoriesUseCase {
        return GetCategoriesUseCase(appContainer.outingRepository)
    }

    fun provideOutingViewModelFactory(): OutingViewModelFactory {
        return OutingViewModelFactory(
            createOutingUseCase = provideCreateOutingUseCase(),
            getCategoriesUseCase = provideGetCategoriesUseCase()
        )
    }
}
