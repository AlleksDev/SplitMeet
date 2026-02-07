package com.coditos.splitmeet.features.detailOuting.di

import com.coditos.splitmeet.core.di.AppContainer
import com.coditos.splitmeet.features.detailOuting.data.repositories.DetailOutingRepositoryImpl
import com.coditos.splitmeet.features.detailOuting.domain.repositories.DetailOutingRepository
import com.coditos.splitmeet.features.detailOuting.domain.usecases.AddParticipantUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.DeleteOutingUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetOutingDetailUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetOutingItemsUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetParticipantsUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.SearchUsersUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.UpdateOutingUseCase
import com.coditos.splitmeet.features.detailOuting.presentation.viewmodels.DetailOutingViewModelFactory
import com.coditos.splitmeet.features.outing.domain.usecases.GetCategoriesUseCase

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

    private fun provideUpdateOutingUseCase(): UpdateOutingUseCase {
        return UpdateOutingUseCase(detailOutingRepository)
    }

    private fun provideDeleteOutingUseCase(): DeleteOutingUseCase {
        return DeleteOutingUseCase(detailOutingRepository)
    }

    private fun provideGetCategoriesUseCase(): GetCategoriesUseCase {
        return GetCategoriesUseCase(appContainer.outingRepository)
    }

    fun provideDetailOutingViewModelFactory(): DetailOutingViewModelFactory {
        return DetailOutingViewModelFactory(
            getOutingDetailUseCase = provideGetOutingDetailUseCase(),
            getParticipantsUseCase = provideGetParticipantsUseCase(),
            getOutingItemsUseCase = provideGetOutingItemsUseCase(),
            searchUsersUseCase = provideSearchUsersUseCase(),
            addParticipantUseCase = provideAddParticipantUseCase(),
            updateOutingUseCase = provideUpdateOutingUseCase(),
            deleteOutingUseCase = provideDeleteOutingUseCase(),
            getCategoriesUseCase = provideGetCategoriesUseCase()
        )
    }
}
