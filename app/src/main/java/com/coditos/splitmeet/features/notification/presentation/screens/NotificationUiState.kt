package com.coditos.splitmeet.features.notification.presentation.screens

import com.coditos.splitmeet.features.notification.domain.entities.Notification
import com.coditos.splitmeet.features.notification.domain.entities.SseConnectionState

data class NotificationUiState(
    val notifications: List<Notification> = emptyList(),
    val connectionState: SseConnectionState = SseConnectionState.Disconnected,
    val unreadCount: Int = 0
)
