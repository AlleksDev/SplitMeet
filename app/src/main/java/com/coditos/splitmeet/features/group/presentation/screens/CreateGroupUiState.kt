package com.coditos.splitmeet.features.group.presentation.screens

data class CreateGroupUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val createdGroupId: Long? = null,
    val error: String? = null,

    // Form fields
    val name: String = "",
    val description: String = "",

    // Validation
    val nameError: String? = null
) {
    val isFormValid: Boolean
        get() = name.isNotBlank()
}
