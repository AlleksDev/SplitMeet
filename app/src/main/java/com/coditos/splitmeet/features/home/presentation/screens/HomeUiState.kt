package com.coditos.splitmeet.features.home.presentation.screens

import com.coditos.splitmeet.features.home.domain.entities.Outing

data class HomeUiState(
    val isLoading: Boolean = true,
    val isSyncing: Boolean = false,
    val isRefreshing: Boolean = false,
    val activeOutings: List<Outing> = emptyList(),
    val historyOutings: List<Outing> = emptyList(),
    val selectedTabIndex: Int = 0,
    val error: String? = null
)