package com.coditos.splitmeet.core.network.sse

sealed class SseConnectionState {
    data object Connecting : SseConnectionState()
    data object Connected : SseConnectionState()
    data class Error(val throwable: Throwable) : SseConnectionState()
    data object Disconnected : SseConnectionState()
}
