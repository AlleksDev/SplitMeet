package com.coditos.splitmeet.core.network.fcm

import android.content.Context
import android.util.Log
import com.coditos.splitmeet.features.notification.data.datasources.remote.api.NotificationApi
import com.coditos.splitmeet.features.notification.data.datasources.remote.model.RegisterDeviceTokenRequest
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages the FCM device token lifecycle:
 * 1. Retrieves the current FCM token from Firebase.
 * 2. Sends it to the backend via `POST /notifications/device-token`.
 *
 * Call [registerTokenWithBackend] after login or whenever the user's session
 * becomes valid to ensure the backend always has a fresh token.
 */
@Singleton
class FcmTokenManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationApi: NotificationApi
) {

    companion object {
        private const val TAG = "FcmTokenManager"
    }

    /**
     * Fetches the current FCM token and registers it with the backend.
     * Safe to call multiple times — the backend upserts the token.
     */
    suspend fun registerTokenWithBackend() {
        try {
            val token = FirebaseMessaging.getInstance().token.await()
            Log.d(TAG, "FCM token obtained: ${token.take(15)}...")

            // Also persist locally for potential future use
            FcmTokenDataStore.saveToken(context, token)

            // Send to backend
            notificationApi.registerDeviceToken(
                RegisterDeviceTokenRequest(token = token, platform = "android")
            )
            Log.d(TAG, "FCM token registered with backend successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error registering FCM token with backend", e)
        }
    }
}
