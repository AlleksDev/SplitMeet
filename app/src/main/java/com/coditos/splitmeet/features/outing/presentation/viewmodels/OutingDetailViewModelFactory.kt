package com.coditos.splitmeet.features.outing.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coditos.splitmeet.features.outing.domain.usecases.GetOutingByIdUseCase

class OutingDetailViewModelFactory(
    private val getOutingByIdUseCase: GetOutingByIdUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OutingDetailViewModel::class.java)) {
            return OutingDetailViewModel(getOutingByIdUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
