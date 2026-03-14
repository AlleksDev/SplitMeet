package com.coditos.splitmeet.features.auth.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.core.network.fcm.FcmTokenManager
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.LoginRequest
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.User
import com.coditos.splitmeet.features.auth.domain.usecases.LoginUseCase
import com.coditos.splitmeet.features.auth.domain.usecases.RegisterUseCase
import com.coditos.splitmeet.features.auth.domain.usecases.SaveTokenUseCase
import com.coditos.splitmeet.features.auth.presentation.screens.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val fcmTokenManager: FcmTokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    // 🔐 LOGIN
    fun login(request: LoginRequest) {
        _uiState.update {
            it.copy(
                isLoading = true,
                error = null,
                isSuccess = false
            )
        }

        viewModelScope.launch {
            val result = loginUseCase(request)
            Log.d("AuthViewModel", "Login result: $result")

            result.fold(
                onSuccess = { response ->
                    saveTokenUseCase(response.token)
                    Log.d("AuthViewModel", "Token saved successfully")

                    // Register FCM device token with backend right after login
                    registerFcmToken()

                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            isSuccess = true,
                            message = response.message
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
            )
        }
    }

    // 📝 REGISTER
    fun register(user: User) {
        _uiState.update {
            it.copy(
                isLoading = true,
                error = null,
                isSuccess = false
            )
        }

        viewModelScope.launch {
            val result = registerUseCase(user)
            Log.d("AuthViewModel", "Register result: $result")

            result.fold(
                onSuccess = { response ->
                    Log.d("AuthViewModel", "Register successful, auto-login...")
                    // Auto-login después del registro exitoso
                    performAutoLogin(
                        email = user.email,
                        password = user.password
                    )
                },
                onFailure = { error ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
            )
        }
    }

    private suspend fun performAutoLogin(email: String, password: String) {
        val loginResult = loginUseCase(LoginRequest(email = email, password = password))
        Log.d("AuthViewModel", "Auto-login result: $loginResult")

        loginResult.fold(
            onSuccess = { response ->
                saveTokenUseCase(response.token)
                Log.d("AuthViewModel", "Token saved after auto-login")

                // Register FCM device token with backend right after auto-login
                registerFcmToken()

                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isSuccess = true,
                        message = "Registro e inicio de sesión exitosos"
                    )
                }
            },
            onFailure = { error ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = "Registro exitoso pero falló el inicio de sesión: ${error.message}"
                    )
                }
            }
        )
    }

    /**
     * Registers the FCM device token with the backend so push notifications
     * can be delivered to this device.
     */
    private fun registerFcmToken() {
        viewModelScope.launch {
            fcmTokenManager.registerTokenWithBackend()
        }
    }

    fun clearState() {
        _uiState.value = AuthUiState()
    }
}
