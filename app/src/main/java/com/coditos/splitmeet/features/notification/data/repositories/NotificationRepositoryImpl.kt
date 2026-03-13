package com.coditos.splitmeet.features.notification.data.repositories

import android.util.Log
import com.coditos.splitmeet.core.hardware.domain.HapticFeedbackManager
import com.coditos.splitmeet.core.network.fcm.NotificationHelper
import com.coditos.splitmeet.core.network.sse.SseStreamManager
import com.coditos.splitmeet.features.notification.data.datasources.remote.api.NotificationApi
import com.coditos.splitmeet.features.notification.data.datasources.remote.mapper.toDomain
import com.coditos.splitmeet.features.notification.data.datasources.remote.model.NotificationDto
import com.coditos.splitmeet.features.notification.data.datasources.remote.model.RegisterDeviceTokenRequest
import com.coditos.splitmeet.features.notification.data.datasources.remote.model.RespondInvitationRequest
import com.coditos.splitmeet.features.notification.domain.entities.Notification
import com.coditos.splitmeet.features.notification.domain.repositories.NotificationRepository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val sseStreamManager: SseStreamManager,
    private val hapticManager: HapticFeedbackManager,
    private val notificationApi: NotificationApi,
    private val notificationHelper: NotificationHelper,
    private val gson: Gson
) : NotificationRepository {

    companion object {
        private const val TAG = "NotificationRepo"
        private val NOTIFICATION_EVENT_TYPES = setOf(
            "notification"
        )
    }

    private val _notifications = MutableSharedFlow<Notification>(
        replay = 0,
        extraBufferCapacity = 64
    )
    override val notifications: SharedFlow<Notification> = _notifications.asSharedFlow()

    private var listenerScope: CoroutineScope? = null
    private var listenerJob: Job? = null

    @Synchronized
    override fun startListening() {
        if (listenerJob?.isActive == true) return

        sseStreamManager.connect()

        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        listenerScope = scope

        listenerJob = scope.launch {
            sseStreamManager.events.collect { event ->
                if (event.type in NOTIFICATION_EVENT_TYPES) {
                    parseAndEmit(event.payload)
                }
            }
        }
    }

    @Synchronized
    override fun stopListening() {
        listenerJob?.cancel()
        listenerJob = null
        listenerScope?.cancel()
        listenerScope = null
    }

    private fun parseAndEmit(json: String) {
        try {
            val dto = gson.fromJson(json, NotificationDto::class.java)
            val notification = dto.toDomain()
            val emitted = _notifications.tryEmit(notification)
            if (emitted) {
                // Vibrate for haptic feedback
                hapticManager.vibrateForNotification()

                // Show Android system notification (heads-up with sound)
                notificationHelper.showNotification(
                    title = notification.title,
                    body = notification.message,
                    notificationId = notification.id.toInt()
                )
            } else {
                Log.w(TAG, "Buffer full — notification ${dto.id} dropped")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing SSE notification: $json", e)
        }
    }

    override suspend fun getNotifications(page: Int, limit: Int): Result<List<Notification>> {
        return try {
            val response = notificationApi.getNotifications(page, limit)
            Result.success(response.data.map { it.toDomain() })
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching notifications", e)
            Result.failure(e)
        }
    }

    override suspend fun respondGroupInvitation(groupId: Long, accept: Boolean): Result<String> {
        return try {
            val response = notificationApi.respondGroupInvitation(
                groupId, RespondInvitationRequest(accept)
            )
            Result.success(response.message)
        } catch (e: Exception) {
            Log.e(TAG, "Error responding to group invitation", e)
            Result.failure(e)
        }
    }

    override suspend fun respondOutingInvitation(outingId: Long, accept: Boolean): Result<String> {
        return try {
            val response = notificationApi.respondOutingInvitation(
                outingId, RespondInvitationRequest(accept)
            )
            Result.success(response.message)
        } catch (e: Exception) {
            Log.e(TAG, "Error responding to outing invitation", e)
            Result.failure(e)
        }
    }

    override suspend fun registerDeviceToken(token: String): Result<String> {
        return try {
            val response = notificationApi.registerDeviceToken(
                RegisterDeviceTokenRequest(token = token, platform = "android")
            )
            Result.success(response.message)
        } catch (e: Exception) {
            Log.e(TAG, "Error registering device token", e)
            Result.failure(e)
        }
    }
}
