package com.coditos.splitmeet.features.profile.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.features.profile.domain.usecases.ProfileUseCases
import com.coditos.splitmeet.features.profile.presentation.screens.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val useCases: ProfileUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            useCases.getProfile().fold(
                onSuccess = { profile ->
                    _uiState.update { it.copy(isLoading = false, profile = profile) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = getFriendlyErrorMessage(e)) }
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            useCases.logout()
            _uiState.update { it.copy(loggedOut = true) }
        }
    }

    fun showEditDialog() {
        val currentProfile = _uiState.value.profile ?: return
        _uiState.update {
            it.copy(
                showEditDialog = true,
                editName = currentProfile.name,
                editPhone = currentProfile.phone,
                editPassword = "",
                error = null
            )
        }
    }

    fun dismissEditDialog() {
        _uiState.update {
            it.copy(
                showEditDialog = false,
                editPassword = ""
            )
        }
    }

    fun onEditNameChanged(name: String) {
        _uiState.update { it.copy(editName = name) }
    }

    fun onEditPhoneChanged(phone: String) {
        _uiState.update { it.copy(editPhone = phone) }
    }

    fun onEditPasswordChanged(password: String) {
        _uiState.update { it.copy(editPassword = password) }
    }

    fun updateProfile() {
        val name = _uiState.value.editName.trim()
        val phone = _uiState.value.editPhone.trim()
        val password = _uiState.value.editPassword.trim()

        if (name.isBlank()) {
            _uiState.update { it.copy(error = "El nombre no puede estar vacio") }
            return
        }

        _uiState.update { it.copy(isUpdating = true, error = null) }
        viewModelScope.launch {
            useCases.updateProfile(name, phone, password).fold(
                onSuccess = { updatedProfile ->
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            profile = updatedProfile,
                            showEditDialog = false,
                            editPassword = ""
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isUpdating = false, error = getFriendlyErrorMessage(e)) }
                }
            )
        }
    }

    private fun getFriendlyErrorMessage(error: Throwable?): String {
        val msg = error?.message?.lowercase() ?: return "Ocurrió un error inesperado."

        return when {
            msg.contains("401") || msg.contains("unauthorized") -> "Tu sesión ha expirado. Por favor, vuelve a iniciar sesión."
            msg.contains("400") || msg.contains("bad request") -> "Verifica que los datos ingresados sean correctos."
            msg.contains("404") -> "No pudimos cargar la información de tu perfil."
            msg.contains("409") || msg.contains("conflict") -> "La información proporcionada ya está en uso por otra cuenta."
            msg.contains("timeout") -> "La conexión tardó demasiado. Revisa tu internet e inténtalo de nuevo."
            msg.contains("network") || msg.contains("unknownhost") || msg.contains("connect") -> "No hay conexión a internet. Revisa tu red."
            msg.contains("500") || msg.contains("internal") -> "Problemas con el servidor. Por favor, inténtalo más tarde."
            else -> "No pudimos completar la acción. Inténtalo de nuevo."
        }
    }
}
