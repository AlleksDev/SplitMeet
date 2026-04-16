package com.coditos.splitmeet.features.group.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.features.group.domain.usecases.GroupUseCases
import com.coditos.splitmeet.features.group.presentation.screens.CreateGroupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val groupUseCases: GroupUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateGroupUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name, nameError = null) }
    }

    fun onDescriptionChanged(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun createGroup() {
        val currentState = _uiState.value

        if (currentState.name.isBlank()) {
            _uiState.update { it.copy(nameError = "El nombre es requerido") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            groupUseCases.createGroup(currentState.name, currentState.description).fold(
                onSuccess = { group ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            createdGroupId = group.id
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = getFriendlyErrorMessage(error)
                        )
                    }
                }
            )
        }
    }

    private fun getFriendlyErrorMessage(error: Throwable?): String {
        val msg = error?.message?.lowercase() ?: return "Ocurrió un error inesperado al intentar crear el grupo."

        return when {
            msg.contains("401") || msg.contains("unauthorized") -> "Tu sesión ha expirado. Por favor, vuelve a iniciar sesión."
            msg.contains("400") || msg.contains("bad request") -> "Verifica que los datos ingresados sean correctos."
            msg.contains("409") || msg.contains("conflict") -> "Ya existe un grupo con un nombre similar. Intenta con otro nombre."
            msg.contains("timeout") -> "La conexión tardó demasiado. Revisa tu internet e inténtalo de nuevo."
            msg.contains("network") || msg.contains("unknownhost") || msg.contains("connect") -> "No hay conexión a internet. Revisa tu red."
            msg.contains("500") || msg.contains("internal") -> "Problemas con el servidor. Por favor, inténtalo más tarde."
            else -> "No pudimos crear el grupo en este momento. Inténtalo de nuevo."
        }
    }

    fun clearState() {
        _uiState.update { CreateGroupUiState() }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
