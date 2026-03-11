package com.coditos.splitmeet.core.hardware.data

import android.app.NotificationManager
import android.content.Context
import com.coditos.splitmeet.core.hardware.domain.AppVibrator
import com.coditos.splitmeet.core.hardware.domain.HapticFeedbackManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidHapticFeedbackManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val vibrator: AppVibrator
) : HapticFeedbackManager {

    companion object {
        private const val NOTIFICATION_VIBRATION_MS = 200L
    }

    override fun vibrateForNotification() {
        if (!vibrator.hasVibrator()) return
        if (isDoNotDisturbActive()) return

        vibrator.vibrate(NOTIFICATION_VIBRATION_MS)
    }

    /**
     * Checks the system's Do Not Disturb (interruption filter).
     * Returns true when DND blocks haptic alerts so the vibration must be suppressed.
     */
    private fun isDoNotDisturbActive(): Boolean {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            ?: return false
        return nm.currentInterruptionFilter != NotificationManager.INTERRUPTION_FILTER_ALL
    }
}
