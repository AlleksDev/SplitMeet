package com.coditos.splitmeet.features.home.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.core.ui.BaseViewModel
import com.coditos.splitmeet.features.home.domain.usecases.GetHomeItemByIdUseCase
import com.coditos.splitmeet.features.home.domain.usecases.GetHomeItemsUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHomeItemsUseCase: GetHomeItemsUseCase,
    private val getHomeItemByIdUseCase: GetHomeItemByIdUseCase
) : BaseViewModel<List<HomeItem>>() {

    init {
        loadHomeItems()
    }

    /**
     * Loads all home items from the API
     */
    fun loadHomeItems() {
        viewModelScope.launch {
            setLoading()
            when (val result = getHomeItemsUseCase()) {
                is NetworkResult.Success -> setSuccess(result.data)
                is NetworkResult.Error -> setError(result.message)
                is NetworkResult.Loading -> setLoading()
            }
        }
    }

    /**
     * Refreshes the home items (pull-to-refresh)
     */
    fun refresh() {
        loadHomeItems()
    }

    /**
     * Retries loading after an error
     */
    fun retry() {
        loadHomeItems()
    }
}