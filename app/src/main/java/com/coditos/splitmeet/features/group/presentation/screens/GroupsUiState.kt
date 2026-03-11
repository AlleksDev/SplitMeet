package com.coditos.splitmeet.features.group.presentation.screens

import com.coditos.splitmeet.features.group.domain.entities.Group

data class GroupsUiState(
    val isLoading: Boolean = false,
    val groups: List<Group> = emptyList(),
    val error: String? = null
)
