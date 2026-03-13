package com.coditos.splitmeet.core.network.sse

import android.util.Log
import com.coditos.splitmeet.BuildConfig
import com.coditos.splitmeet.core.di.SseClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Global, generic SSE stream manager.
 *
 * - Opens exactly ONE connection to `/notifications/stream`.
 * - Exposes a [SharedFlow] of [SseEvent] that any feature can filter by `type`.
 * - Handles exponential-backoff reconnection internally.
 * - Thread-safe: usable from any coroutine scope or thread.
 */
@Singleton
class SseStreamManager @Inject constructor(
    @SseClient private val sseClient: OkHttpClient
) {

    companion object {
        private const val TAG = "SseStreamManager"
        private const val SSE_PATH = "notifications/stream"
        private const val INITIAL_RETRY_MS = 1_000L
        private const val MAX_RETRY_MS = 30_000L
        private const val BACKOFF_MULTIPLIER = 2.0
    }

    // ── Public API ───────────────────────────────────────────────────────

    private val _events = MutableSharedFlow<SseEvent>(
        replay = 0,
        extraBufferCapacity = 64
    )
    val events: SharedFlow<SseEvent> = _events.asSharedFlow()

    private val _connectionState = MutableStateFlow<SseConnectionState>(SseConnectionState.Disconnected)
    val connectionState: StateFlow<SseConnectionState> = _connectionState.asStateFlow()

    // ── Internal state ───────────────────────────────────────────────────

    private var eventSource: EventSource? = null
    private val isActive = AtomicBoolean(false)
    private var retryDelay = INITIAL_RETRY_MS

    // ── Lifecycle ────────────────────────────────────────────────────────

    @Synchronized
    fun connect() {
        if (isActive.get()) return
        isActive.set(true)
        retryDelay = INITIAL_RETRY_MS
        openConnection()
    }

    @Synchronized
    fun disconnect() {
        isActive.set(false)
        closeConnection()
        _connectionState.value = SseConnectionState.Disconnected
    }

    // ── Connection management ────────────────────────────────────────────

    private fun openConnection() {
        closeConnection()
        _connectionState.value = SseConnectionState.Connecting
        Log.d(TAG, "Opening SSE → ${BuildConfig.BASE_URL}$SSE_PATH")

        val request = Request.Builder()
            .url("${BuildConfig.BASE_URL}$SSE_PATH")
            .header("Accept", "text/event-stream")
            .build()

        eventSource = EventSources.createFactory(sseClient)
            .newEventSource(request, listener)
    }

    private fun closeConnection() {
        eventSource?.cancel()
        eventSource = null
    }

    private fun scheduleReconnect() {
        if (!isActive.get()) return

        val delay = retryDelay
        retryDelay = (retryDelay * BACKOFF_MULTIPLIER).toLong().coerceAtMost(MAX_RETRY_MS)

        Log.d(TAG, "Reconnecting in ${delay}ms")
        _connectionState.value = SseConnectionState.Connecting

        sseClient.dispatcher.executorService.submit {
            try {
                TimeUnit.MILLISECONDS.sleep(delay)
                if (isActive.get()) openConnection()
            } catch (_: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    // ── EventSourceListener ──────────────────────────────────────────────

    private val listener = object : EventSourceListener() {

        override fun onOpen(eventSource: EventSource, response: Response) {
            Log.d(TAG, "SSE OPEN")
            retryDelay = INITIAL_RETRY_MS
            _connectionState.value = SseConnectionState.Connected
        }

        override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
            if (type.isNullOrEmpty() || type == "connected") {
                Log.d(TAG, "SSE handshake/connected: $data")
                return
            }
            val emitted = _events.tryEmit(SseEvent(type = type, payload = data))
            if (!emitted) {
                Log.w(TAG, "SharedFlow buffer full — SSE event '$type' dropped")
            }
        }

        override fun onClosed(eventSource: EventSource) {
            Log.d(TAG, "SSE CLOSED by server")
            scheduleReconnect()
        }

        override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
            val msg = t?.message ?: "HTTP ${response?.code}"
            Log.e(TAG, "SSE FAILURE: $msg", t)
            _connectionState.value = SseConnectionState.Error(t ?: Exception("SSE failure: $msg"))
            scheduleReconnect()
        }
    }
}
