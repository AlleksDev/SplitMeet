package com.coditos.splitmeet.features.notification.presentation.screens

import com.coditos.splitmeet.features.notification.domain.entities.Notification

data class NotificationUiState(
    val isLoading: Boolean = true,
    val notifications: List<Notification> = emptyList(),
    val error: String? = null,
    val respondingIds: Set<Long> = emptySet(),
    val acceptedIds: Set<Long> = emptySet(),
    val rejectedIds: Set<Long> = emptySet()
)
