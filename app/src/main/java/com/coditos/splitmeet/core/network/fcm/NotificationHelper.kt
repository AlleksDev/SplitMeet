package com.coditos.splitmeet.core.network.fcm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.coditos.splitmeet.MainActivity
import com.coditos.splitmeet.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Creates Android system (heads-up) notifications.
 * Used by both SSE real-time events and foreground FCM messages.
 */
@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val CHANNEL_ID = "splitmeet_alerts_high"
    }

    /**
     * Show an Android system notification with sound, vibration, and heads-up display.
     */
    fun showNotification(
        title: String,
        body: String,
        notificationId: Int = System.currentTimeMillis().toInt(),
        data: Map<String, String> = emptyMap()
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            data.forEach { (key, value) -> putExtra(key, value) }
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(notificationId, notification)
    }
}
