package com.coditos.splitmeet.features.outing.di

import com.coditos.splitmeet.core.di.AppContainer
import com.coditos.splitmeet.features.outing.domain.usecases.CreateOutingUseCase
import com.coditos.splitmeet.features.outing.domain.usecases.GetActiveOutingsUseCase
import com.coditos.splitmeet.features.outing.domain.usecases.GetOutingByIdUseCase
import com.coditos.splitmeet.features.outing.domain.usecases.GetOutingHistoryUseCase
import com.coditos.splitmeet.features.outing.presentation.viewmodels.CreateOutingViewModelFactory
import com.coditos.splitmeet.features.outing.presentation.viewmodels.OutingDetailViewModelFactory
import com.coditos.splitmeet.features.outing.presentation.viewmodels.OutingListViewModelFactory

/**
 * Module for Outing feature - provides ViewModel factories
 */
class OutingModule(private val appContainer: AppContainer) {

    // Use Cases
    private val getActiveOutingsUseCase: GetActiveOutingsUseCase by lazy {
        GetActiveOutingsUseCase(appContainer.outingRepository)
    }

    private val getOutingHistoryUseCase: GetOutingHistoryUseCase by lazy {
        GetOutingHistoryUseCase(appContainer.outingRepository)
    }

    private val createOutingUseCase: CreateOutingUseCase by lazy {
        CreateOutingUseCase(appContainer.outingRepository)
    }

    private val getOutingByIdUseCase: GetOutingByIdUseCase by lazy {
        GetOutingByIdUseCase(appContainer.outingRepository)
    }

    // ViewModel Factories
    fun provideOutingListViewModelFactory(): OutingListViewModelFactory {
        return OutingListViewModelFactory(
            getActiveOutingsUseCase,
            getOutingHistoryUseCase
        )
    }

    fun provideCreateOutingViewModelFactory(): CreateOutingViewModelFactory {
        return CreateOutingViewModelFactory(createOutingUseCase)
    }

    fun provideOutingDetailViewModelFactory(): OutingDetailViewModelFactory {
        return OutingDetailViewModelFactory(getOutingByIdUseCase)
    }
}
