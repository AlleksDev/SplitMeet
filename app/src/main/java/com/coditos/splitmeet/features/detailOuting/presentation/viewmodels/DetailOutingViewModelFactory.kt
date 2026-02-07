package com.coditos.splitmeet.features.detailOuting.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coditos.splitmeet.features.detailOuting.domain.usecases.AddParticipantUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.DeleteOutingUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetOutingDetailUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetOutingItemsUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetParticipantsUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.SearchUsersUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.UpdateOutingUseCase
import com.coditos.splitmeet.features.outing.domain.usecases.GetCategoriesUseCase

class DetailOutingViewModelFactory(
    private val getOutingDetailUseCase: GetOutingDetailUseCase,
    private val getParticipantsUseCase: GetParticipantsUseCase,
    private val getOutingItemsUseCase: GetOutingItemsUseCase,
    private val searchUsersUseCase: SearchUsersUseCase,
    private val addParticipantUseCase: AddParticipantUseCase,
    private val updateOutingUseCase: UpdateOutingUseCase,
    private val deleteOutingUseCase: DeleteOutingUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailOutingViewModel::class.java)) {
            return DetailOutingViewModel(
                getOutingDetailUseCase = getOutingDetailUseCase,
                getParticipantsUseCase = getParticipantsUseCase,
                getOutingItemsUseCase = getOutingItemsUseCase,
                searchUsersUseCase = searchUsersUseCase,
                addParticipantUseCase = addParticipantUseCase,
                updateOutingUseCase = updateOutingUseCase,
                deleteOutingUseCase = deleteOutingUseCase,
                getCategoriesUseCase = getCategoriesUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
