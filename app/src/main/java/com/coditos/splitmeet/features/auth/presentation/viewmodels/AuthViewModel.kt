package com.coditos.splitmeet.features.auth.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.core.network.fcm.FcmTokenManager
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.LoginRequest
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.User
import com.coditos.splitmeet.features.auth.domain.usecases.AuthUseCases
import com.coditos.splitmeet.features.auth.presentation.screens.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val useCases: AuthUseCases,
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
            val result = useCases.login(request)
            Log.d("AuthViewModel", "Login result: $result")

            result.fold(
                onSuccess = { response ->
                    useCases.saveToken(response.token)
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
                            error = getFriendlyErrorMessage(error)
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
            val result = useCases.register(user)
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
                            error = getFriendlyErrorMessage(error)
                        )
                    }
                }
            )
        }
    }

    private suspend fun performAutoLogin(email: String, password: String) {
        val loginResult = useCases.login(LoginRequest(email = email, password = password))
        Log.d("AuthViewModel", "Auto-login result: $loginResult")

        loginResult.fold(
            onSuccess = { response ->
                useCases.saveToken(response.token)
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
                        error = "Registro exitoso, pero no pudimos iniciar sesión: ${getFriendlyErrorMessage(error)}"
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

    private fun getFriendlyErrorMessage(error: Throwable?): String {
        val msg = error?.message?.lowercase() ?: return "Ocurrió un error inesperado. Por favor, inténtalo de nuevo."
        
        return when {
            msg.contains("401") || msg.contains("unauthorized") -> "Correo electrónico o contraseña incorrectos."
            msg.contains("400") || msg.contains("bad request") -> "Por favor, verifica que los datos ingresados sean correctos."
            msg.contains("404") -> "Usuario no encontrado."
            msg.contains("409") || msg.contains("conflict") -> "Ya existe una cuenta con estos datos."
            msg.contains("timeout") -> "La conexión tardó demasiado. Revisa tu internet e inténtalo de nuevo."
            msg.contains("network") || msg.contains("unknownhost") || msg.contains("connect") -> "No hay conexión a internet. Revisa tu red."
            msg.contains("500") || msg.contains("internal") -> "Problemas con el servidor. Por favor, inténtalo más tarde."
            else -> "No pudimos completar la acción. Inténtalo de nuevo."
        }
    }

    fun clearState() {
        _uiState.value = AuthUiState()
    }
}
