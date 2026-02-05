package com.coditos.splitmeet.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coditos.splitmeet.features.home.domain.usecases.GetHomeItemByIdUseCase
import com.coditos.splitmeet.features.home.domain.usecases.GetHomeItemsUseCase

class HomeViewModelFactory(
    private val getHomeItemsUseCase: GetHomeItemsUseCase,
    private val getHomeItemByIdUseCase: GetHomeItemByIdUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(getHomeItemsUseCase, getHomeItemByIdUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}