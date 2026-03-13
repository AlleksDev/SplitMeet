package com.coditos.splitmeet.features.profile.presentation.screens

import com.coditos.splitmeet.features.profile.domain.entities.UserProfile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val profile: UserProfile? = null,
    val error: String? = null,
    val loggedOut: Boolean = false,
    val showEditDialog: Boolean = false,
    val editName: String = "",
    val editPhone: String = "",
    val editPassword: String = ""
)
