package com.coditos.splitmeet.core.network.sse

/**
 * Represents a raw SSE event received from the server.
 * Consumers filter by [type] and parse [payload] with Gson/etc.
 */
data class SseEvent(
    val type: String,
    val payload: String
)
