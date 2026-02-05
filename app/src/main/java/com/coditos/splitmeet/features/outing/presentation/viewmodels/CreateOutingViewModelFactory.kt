package com.coditos.splitmeet.features.outing.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coditos.splitmeet.features.outing.domain.usecases.CreateOutingUseCase

class CreateOutingViewModelFactory(
    private val createOutingUseCase: CreateOutingUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateOutingViewModel::class.java)) {
            return CreateOutingViewModel(createOutingUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
