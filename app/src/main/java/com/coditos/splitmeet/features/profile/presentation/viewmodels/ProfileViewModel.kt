package com.coditos.splitmeet.features.profile.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.features.profile.domain.usecases.GetProfileUseCase
import com.coditos.splitmeet.features.profile.domain.usecases.LogoutUseCase
import com.coditos.splitmeet.features.profile.domain.usecases.UpdateProfileUseCase
import com.coditos.splitmeet.features.profile.presentation.screens.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getProfileUseCase().fold(
                onSuccess = { profile ->
                    _uiState.update { it.copy(isLoading = false, profile = profile) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
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
            updateProfileUseCase(name, phone, password).fold(
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
                    _uiState.update { it.copy(isUpdating = false, error = e.message) }
                }
            )
        }
    }
}
