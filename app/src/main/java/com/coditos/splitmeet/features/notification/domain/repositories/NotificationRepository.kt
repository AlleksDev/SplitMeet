package com.coditos.splitmeet.features.notification.domain.repositories

import com.coditos.splitmeet.features.notification.domain.entities.Notification
import com.coditos.splitmeet.features.notification.domain.entities.SseConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface NotificationRepository {

    /** Emits each notification pushed by the SSE stream as it arrives. */
    val notifications: SharedFlow<Notification>

    /** Emits the current SSE connection state (Connecting, Connected, Error, Disconnected). */
    val connectionState: Flow<SseConnectionState>

    /** Opens the SSE connection. Safe to call multiple times — will no-op if already connected. */
    fun connect()

    /** Closes the SSE connection and releases resources. */
    fun disconnect()
}
