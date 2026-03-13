package com.coditos.splitmeet.core.network.fcm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.coditos.splitmeet.MainActivity
import com.coditos.splitmeet.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Handles incoming FCM messages and token refreshes.
 *
 * - Background messages: Android shows the notification automatically using the
 *   `notification` payload and channel `splitmeet_alerts_high`.
 * - Foreground messages: [onMessageReceived] builds a local notification so the
 *   user still sees it.
 * - Token refresh: [onNewToken] persists the token in DataStore for later
 *   registration with the backend.
 */
class SplitMeetFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FCMService"
        private const val CHANNEL_ID = "splitmeet_alerts_high"
    }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // ── Token refresh ───────────────────────────────────────────────────

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM token: $token")

        // Save token to DataStore so FcmTokenManager can pick it up
        serviceScope.launch {
            try {
                FcmTokenDataStore.saveToken(applicationContext, token)
                Log.d(TAG, "FCM token saved to DataStore")
            } catch (e: Exception) {
                Log.e(TAG, "Error saving FCM token", e)
            }
        }
    }

    // ── Foreground message handling ──────────────────────────────────────

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "Message received: type=${message.data["type"]}")

        val title = message.notification?.title
            ?: message.data["title"]
            ?: "SplitMeet"
        val body = message.notification?.body
            ?: message.data["body"]
            ?: return

        showNotification(title, body, message.data)
    }

    private fun showNotification(title: String, body: String, data: Map<String, String>) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            data.forEach { (key, value) -> putExtra(key, value) }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
