package com.coditos.splitmeet.core.hardware.domain

interface HapticFeedbackManager {
    /** Short notification pulse. Respects Do Not Disturb mode. */
    fun vibrateForNotification()
}
