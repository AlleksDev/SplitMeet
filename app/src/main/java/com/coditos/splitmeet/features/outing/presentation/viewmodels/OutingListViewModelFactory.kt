package com.coditos.splitmeet.features.outing.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coditos.splitmeet.features.outing.domain.usecases.GetActiveOutingsUseCase
import com.coditos.splitmeet.features.outing.domain.usecases.GetOutingHistoryUseCase

class OutingListViewModelFactory(
    private val getActiveOutingsUseCase: GetActiveOutingsUseCase,
    private val getOutingHistoryUseCase: GetOutingHistoryUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OutingListViewModel::class.java)) {
            return OutingListViewModel(getActiveOutingsUseCase, getOutingHistoryUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
