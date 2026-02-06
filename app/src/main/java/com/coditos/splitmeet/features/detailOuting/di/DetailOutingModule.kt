package com.coditos.splitmeet.features.detailOuting.di

import com.coditos.splitmeet.core.di.AppContainer
import com.coditos.splitmeet.features.detailOuting.data.repositories.DetailOutingRepositoryImpl
import com.coditos.splitmeet.features.detailOuting.domain.repositories.DetailOutingRepository
import com.coditos.splitmeet.features.detailOuting.domain.usecases.AddParticipantUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetOutingDetailUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetOutingItemsUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetParticipantsUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.SearchUsersUseCase
import com.coditos.splitmeet.features.detailOuting.presentation.viewmodels.DetailOutingViewModelFactory

class DetailOutingModule(
    private val appContainer: AppContainer
) {
    private val detailOutingRepository: DetailOutingRepository by lazy {
        DetailOutingRepositoryImpl(appContainer.splitMeetApi)
    }

    private fun provideGetOutingDetailUseCase(): GetOutingDetailUseCase {
        return GetOutingDetailUseCase(detailOutingRepository)
    }

    private fun provideGetParticipantsUseCase(): GetParticipantsUseCase {
        return GetParticipantsUseCase(detailOutingRepository)
    }

    private fun provideGetOutingItemsUseCase(): GetOutingItemsUseCase {
        return GetOutingItemsUseCase(detailOutingRepository)
    }

    private fun provideSearchUsersUseCase(): SearchUsersUseCase {
        return SearchUsersUseCase(detailOutingRepository)
    }

    private fun provideAddParticipantUseCase(): AddParticipantUseCase {
        return AddParticipantUseCase(detailOutingRepository)
    }

    fun provideDetailOutingViewModelFactory(): DetailOutingViewModelFactory {
        return DetailOutingViewModelFactory(
            getOutingDetailUseCase = provideGetOutingDetailUseCase(),
            getParticipantsUseCase = provideGetParticipantsUseCase(),
            getOutingItemsUseCase = provideGetOutingItemsUseCase(),
            searchUsersUseCase = provideSearchUsersUseCase(),
            addParticipantUseCase = provideAddParticipantUseCase()
        )
    }
}
