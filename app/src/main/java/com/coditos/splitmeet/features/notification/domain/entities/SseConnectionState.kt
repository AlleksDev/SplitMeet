package com.coditos.splitmeet.features.notification.domain.entities

sealed class SseConnectionState {
    data object Connecting : SseConnectionState()
    data object Connected : SseConnectionState()
    data class Error(val throwable: Throwable) : SseConnectionState()
    data object Disconnected : SseConnectionState()
}
