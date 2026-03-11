package com.coditos.splitmeet.features.notification.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.features.notification.domain.usecases.ConnectSseUseCase
import com.coditos.splitmeet.features.notification.domain.usecases.DisconnectSseUseCase
import com.coditos.splitmeet.features.notification.domain.usecases.ObserveNotificationsUseCase
import com.coditos.splitmeet.features.notification.domain.usecases.ObserveSseConnectionUseCase
import com.coditos.splitmeet.features.notification.presentation.screens.NotificationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val observeNotifications: ObserveNotificationsUseCase,
    private val observeConnection: ObserveSseConnectionUseCase,
    private val connectSse: ConnectSseUseCase,
    private val disconnectSse: DisconnectSseUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        startListening()
    }

    private fun startListening() {
        connectSse()

        observeNotifications()
            .onEach { notification ->
                _uiState.update { current ->
                    val updated = listOf(notification) + current.notifications
                    current.copy(
                        notifications = updated,
                        unreadCount = updated.count { !it.isRead }
                    )
                }
            }
            .launchIn(viewModelScope)

        observeConnection()
            .onEach { state ->
                _uiState.update { it.copy(connectionState = state) }
            }
            .launchIn(viewModelScope)
    }

    fun reconnect() {
        connectSse()
    }

    override fun onCleared() {
        super.onCleared()
        disconnectSse()
    }
}
