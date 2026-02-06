package com.coditos.splitmeet.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.features.home.domain.usecases.GetHomeItemsUseCase
import com.coditos.splitmeet.features.home.presentation.screens.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.util.Log


class HomeViewModel(
    private val getHomeItemsUseCase: GetHomeItemsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadHomeItems()
    }

    private fun loadHomeItems(isRefresh: Boolean = false) {
        _uiState.update { 
            it.copy(
                isLoading = !isRefresh, 
                isRefreshing = isRefresh,
                error = null
            ) 
        }

        viewModelScope.launch {
            val result = getHomeItemsUseCase()
            Log.d("HomeViewModel", "Result crudo: $result")

            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { list ->
                        currentState.copy(
                            isLoading = false,
                            isRefreshing = false,
                            outings = list
                        )

                    },
                    onFailure = { error ->
                        currentState.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = error.message
                        )
                    }
                )
            }

        }
    }

    fun onRefresh() {
        loadHomeItems(isRefresh = true)
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }
}
