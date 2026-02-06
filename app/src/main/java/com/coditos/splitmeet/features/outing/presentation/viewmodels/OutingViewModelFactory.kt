package com.coditos.splitmeet.features.outing.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coditos.splitmeet.features.outing.domain.usecases.CreateOutingUseCase
import com.coditos.splitmeet.features.outing.domain.usecases.GetCategoriesUseCase

class OutingViewModelFactory(
    private val createOutingUseCase: CreateOutingUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OutingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OutingViewModel(createOutingUseCase, getCategoriesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
