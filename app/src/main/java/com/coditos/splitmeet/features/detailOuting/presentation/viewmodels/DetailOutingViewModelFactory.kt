package com.coditos.splitmeet.features.detailOuting.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coditos.splitmeet.features.detailOuting.domain.usecases.AddParticipantUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetOutingDetailUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetOutingItemsUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetParticipantsUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.SearchUsersUseCase

class DetailOutingViewModelFactory(
    private val getOutingDetailUseCase: GetOutingDetailUseCase,
    private val getParticipantsUseCase: GetParticipantsUseCase,
    private val getOutingItemsUseCase: GetOutingItemsUseCase,
    private val searchUsersUseCase: SearchUsersUseCase,
    private val addParticipantUseCase: AddParticipantUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailOutingViewModel::class.java)) {
            return DetailOutingViewModel(
                getOutingDetailUseCase = getOutingDetailUseCase,
                getParticipantsUseCase = getParticipantsUseCase,
                getOutingItemsUseCase = getOutingItemsUseCase,
                searchUsersUseCase = searchUsersUseCase,
                addParticipantUseCase = addParticipantUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
