package com.coditos.splitmeet.features.outing.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.core.ui.UiState
import com.coditos.splitmeet.features.outing.domain.entities.Outing
import com.coditos.splitmeet.features.outing.domain.usecases.GetActiveOutingsUseCase
import com.coditos.splitmeet.features.outing.domain.usecases.GetOutingHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Outing list screen
 */
class OutingListViewModel(
    private val getActiveOutingsUseCase: GetActiveOutingsUseCase,
    private val getOutingHistoryUseCase: GetOutingHistoryUseCase
) : ViewModel() {

    private val _activeOutingsState = MutableStateFlow<UiState<List<Outing>>>(UiState.idle())
    val activeOutingsState: StateFlow<UiState<List<Outing>>> = _activeOutingsState.asStateFlow()

    private val _historyOutingsState = MutableStateFlow<UiState<List<Outing>>>(UiState.idle())
    val historyOutingsState: StateFlow<UiState<List<Outing>>> = _historyOutingsState.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    init {
        loadActiveOutings()
    }

    fun onTabSelected(index: Int) {
        _selectedTab.value = index
        when (index) {
            0 -> loadActiveOutings()
            1 -> loadOutingHistory()
        }
    }

    fun loadActiveOutings() {
        viewModelScope.launch {
            _activeOutingsState.value = UiState.loading()
            when (val result = getActiveOutingsUseCase()) {
                is NetworkResult.Success -> _activeOutingsState.value = UiState.success(result.data)
                is NetworkResult.Error -> _activeOutingsState.value = UiState.error(result.message)
                is NetworkResult.Loading -> _activeOutingsState.value = UiState.loading()
            }
        }
    }

    fun loadOutingHistory() {
        viewModelScope.launch {
            _historyOutingsState.value = UiState.loading()
            when (val result = getOutingHistoryUseCase()) {
                is NetworkResult.Success -> _historyOutingsState.value = UiState.success(result.data)
                is NetworkResult.Error -> _historyOutingsState.value = UiState.error(result.message)
                is NetworkResult.Loading -> _historyOutingsState.value = UiState.loading()
            }
        }
    }

    fun refresh() {
        when (_selectedTab.value) {
            0 -> loadActiveOutings()
            1 -> loadOutingHistory()
        }
    }
}
