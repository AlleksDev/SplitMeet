package com.coditos.splitmeet.core.session.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.core.network.fcm.FcmTokenManager
import com.coditos.splitmeet.core.session.domain.model.AppStartDestination
import com.coditos.splitmeet.core.session.domain.usecases.ResolveStartDestinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SessionGateViewModel @Inject constructor(
    private val resolveStartDestinationUseCase: ResolveStartDestinationUseCase,
    private val fcmTokenManager: FcmTokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<SessionGateState>(SessionGateState.Loading)
    val uiState: StateFlow<SessionGateState> = _uiState.asStateFlow()

    init {
        resolveStartDestination()
    }

    private fun resolveStartDestination() {
        viewModelScope.launch {
            val destination = resolveStartDestinationUseCase()
            _uiState.value = SessionGateState.Ready(destination)

            // If user is already authenticated, register FCM token with backend
            if (destination == AppStartDestination.HOME) {
                registerFcmToken()
            }
        }
    }

    private fun registerFcmToken() {
        viewModelScope.launch {
            fcmTokenManager.registerTokenWithBackend()
        }
    }
}
