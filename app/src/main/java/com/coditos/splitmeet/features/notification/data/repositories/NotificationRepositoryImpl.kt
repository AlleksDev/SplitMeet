package com.coditos.splitmeet.features.notification.data.repositories

import android.util.Log
import com.coditos.splitmeet.BuildConfig
import com.coditos.splitmeet.core.di.SseClient
import com.coditos.splitmeet.features.notification.data.datasources.remote.mapper.toDomain
import com.coditos.splitmeet.features.notification.data.datasources.remote.model.NotificationDto
import com.coditos.splitmeet.features.notification.domain.entities.Notification
import com.coditos.splitmeet.features.notification.domain.entities.SseConnectionState
import com.coditos.splitmeet.features.notification.domain.repositories.NotificationRepository
import com.google.gson.Gson
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

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    @SseClient private val sseClient: OkHttpClient,
    private val gson: Gson
) : NotificationRepository {

    companion object {
        private const val TAG = "NotificationSSE"
        private const val SSE_PATH = "notifications/stream"
        private const val INITIAL_RETRY_DELAY_MS = 1_000L
        private const val MAX_RETRY_DELAY_MS = 30_000L
        private const val RETRY_BACKOFF_MULTIPLIER = 2.0
    }

    /**
     * SharedFlow con extraBufferCapacity = 64 para no perder eventos si la UI
     * tarda en recolectar. replay = 0 porque las notificaciones son efímeras
     * y un nuevo colector no necesita recibir las anteriores.
     */
    private val _notifications = MutableSharedFlow<Notification>(
        replay = 0,
        extraBufferCapacity = 64
    )
    override val notifications: SharedFlow<Notification> = _notifications.asSharedFlow()

    private val _connectionState = MutableStateFlow<SseConnectionState>(SseConnectionState.Disconnected)
    override val connectionState: StateFlow<SseConnectionState> = _connectionState.asStateFlow()

    private var eventSource: EventSource? = null
    private val isConnected = AtomicBoolean(false)
    private var currentRetryDelay = INITIAL_RETRY_DELAY_MS

    // ── Lifecycle ────────────────────────────────────────────────────────

    @Synchronized
    override fun connect() {
        if (isConnected.get()) return
        isConnected.set(true)
        currentRetryDelay = INITIAL_RETRY_DELAY_MS
        openConnection()
    }

    @Synchronized
    override fun disconnect() {
        isConnected.set(false)
        closeConnection()
        _connectionState.value = SseConnectionState.Disconnected
    }

    // ── Internal ─────────────────────────────────────────────────────────

    private fun openConnection() {
        closeConnection()

        _connectionState.value = SseConnectionState.Connecting
        Log.d(TAG, "Opening SSE connection to $SSE_PATH")

        val request = Request.Builder()
            .url("${BuildConfig.BASE_URL}$SSE_PATH")
            .header("Accept", "text/event-stream")
            .build()

        val factory = EventSources.createFactory(sseClient)
        eventSource = factory.newEventSource(request, sseEventListener)
    }

    private fun closeConnection() {
        eventSource?.cancel()
        eventSource = null
    }

    private fun scheduleReconnect() {
        if (!isConnected.get()) return

        val delay = currentRetryDelay
        currentRetryDelay = (currentRetryDelay * RETRY_BACKOFF_MULTIPLIER)
            .toLong()
            .coerceAtMost(MAX_RETRY_DELAY_MS)

        Log.d(TAG, "Scheduling reconnection in ${delay}ms")
        _connectionState.value = SseConnectionState.Connecting

        sseClient.dispatcher.executorService.submit {
            try {
                TimeUnit.MILLISECONDS.sleep(delay)
                if (isConnected.get()) {
                    openConnection()
                }
            } catch (_: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    // ── EventSource listener ─────────────────────────────────────────────

    private val sseEventListener = object : EventSourceListener() {

        override fun onOpen(eventSource: EventSource, response: Response) {
            Log.d(TAG, "SSE connection OPEN")
            currentRetryDelay = INITIAL_RETRY_DELAY_MS
            _connectionState.value = SseConnectionState.Connected
        }

        override fun onEvent(
            eventSource: EventSource,
            id: String?,
            type: String?,
            data: String
        ) {
            when (type) {
                "connected" -> {
                    Log.d(TAG, "Server confirmed SSE connection: $data")
                }
                "notification" -> {
                    parseAndEmit(data)
                }
                else -> {
                    Log.d(TAG, "Unknown SSE event type: $type, data: $data")
                }
            }
        }

        override fun onClosed(eventSource: EventSource) {
            Log.d(TAG, "SSE connection CLOSED by server")
            scheduleReconnect()
        }

        override fun onFailure(
            eventSource: EventSource,
            t: Throwable?,
            response: Response?
        ) {
            val errorMsg = t?.message ?: "HTTP ${response?.code}"
            Log.e(TAG, "SSE connection FAILURE: $errorMsg", t)
            _connectionState.value = SseConnectionState.Error(
                t ?: Exception("SSE failure: $errorMsg")
            )
            scheduleReconnect()
        }
    }

    private fun parseAndEmit(data: String) {
        try {
            val dto = gson.fromJson(data, NotificationDto::class.java)
            val notification = dto.toDomain()
            val emitted = _notifications.tryEmit(notification)
            if (!emitted) {
                Log.w(TAG, "SharedFlow buffer full — notification ${dto.id} dropped")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing SSE notification payload: $data", e)
        }
    }
}
