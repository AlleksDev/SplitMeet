package com.coditos.splitmeet.features.group.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.features.group.domain.usecases.CreateGroupUseCase
import com.coditos.splitmeet.features.group.presentation.screens.CreateGroupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val createGroupUseCase: CreateGroupUseCase
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
            createGroupUseCase(currentState.name, currentState.description).fold(
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
                            error = error.message ?: "Error al crear el grupo"
                        )
                    }
                }
            )
        }
    }

    fun clearState() {
        _uiState.update { CreateGroupUiState() }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
