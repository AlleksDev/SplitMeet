package com.coditos.splitmeet.features.notification.presentation.screens

import com.coditos.splitmeet.features.notification.domain.entities.Notification

data class NotificationUiState(
    val isLoading: Boolean = true,
    val notifications: List<Notification> = emptyList(),
    val error: String? = null,
    /** IDs of notifications currently being responded to (accept/reject in flight). */
    val respondingIds: Set<Long> = emptySet(),
    /** IDs of notifications already accepted. */
    val acceptedIds: Set<Long> = emptySet(),
    /** IDs of notifications already rejected. */
    val rejectedIds: Set<Long> = emptySet()
)
