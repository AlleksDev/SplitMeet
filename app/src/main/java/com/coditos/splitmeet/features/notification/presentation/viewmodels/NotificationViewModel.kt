package com.coditos.splitmeet.features.notification.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.core.network.fcm.FcmTokenManager
import com.coditos.splitmeet.features.notification.domain.entities.Notification
import com.coditos.splitmeet.features.notification.domain.entities.NotificationType
import com.coditos.splitmeet.features.notification.domain.usecases.GetNotificationsUseCase
import com.coditos.splitmeet.features.notification.domain.usecases.ObserveNotificationsUseCase
import com.coditos.splitmeet.features.notification.domain.usecases.RespondInvitationUseCase
import com.coditos.splitmeet.features.notification.presentation.screens.NotificationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val observeNotifications: ObserveNotificationsUseCase,
    private val respondInvitation: RespondInvitationUseCase,
    private val fcmTokenManager: FcmTokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeNotifications.startListening()
        loadNotifications()
        listenForRealTimeNotifications()
        registerFcmToken()
    }

    override fun onCleared() {
        super.onCleared()
        observeNotifications.stopListening()
    }

    private fun registerFcmToken() {
        viewModelScope.launch {
            fcmTokenManager.registerTokenWithBackend()
        }
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getNotificationsUseCase(page = 1, limit = 50).fold(
                onSuccess = { list ->
                    _uiState.update { it.copy(isLoading = false, notifications = list) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    private fun listenForRealTimeNotifications() {
        observeNotifications()
            .onEach { notification ->
                _uiState.update { current ->
                    current.copy(
                        notifications = listOf(notification) + current.notifications
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun respondToInvitation(notification: Notification, accept: Boolean) {
        val refId = notification.referenceId ?: return

        _uiState.update { it.copy(respondingIds = it.respondingIds + notification.id) }

        viewModelScope.launch {
            val result = when (notification.type) {
                NotificationType.GROUP_INVITATION ->
                    respondInvitation.respondGroup(refId, accept)
                NotificationType.OUTING_INVITATION ->
                    respondInvitation.respondOuting(refId, accept)
                else -> return@launch
            }

            result.fold(
                onSuccess = {
                    _uiState.update { current ->
                        val newAccepted = if (accept) current.acceptedIds + notification.id else current.acceptedIds
                        val newRejected = if (!accept) current.rejectedIds + notification.id else current.rejectedIds
                        current.copy(
                            respondingIds = current.respondingIds - notification.id,
                            acceptedIds = newAccepted,
                            rejectedIds = newRejected
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update { current ->
                        current.copy(
                            respondingIds = current.respondingIds - notification.id,
                            error = e.message
                        )
                    }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun refresh() {
        loadNotifications()
    }
}
