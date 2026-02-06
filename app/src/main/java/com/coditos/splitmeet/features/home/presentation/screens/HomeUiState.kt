package com.coditos.splitmeet.features.home.presentation.screens

import com.coditos.splitmeet.features.home.domain.entities.Outing

data class HomeUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val outings: List<Outing> = emptyList(),
    val selectedTabIndex: Int = 0,
    val error: String? = null
)