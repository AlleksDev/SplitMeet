package com.coditos.splitmeet.core.hardware.data

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.coditos.splitmeet.core.hardware.domain.AppVibrator
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidAppVibrator @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AppVibrator {

    private val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    override fun vibrate(durationMs: Long) {
        val effect = VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator.vibrate(effect)
    }

    override fun vibratePattern(pattern: LongArray, repeat: Int) {
        val effect = VibrationEffect.createWaveform(pattern, repeat)
        vibrator.vibrate(effect)
    }

    override fun cancel() {
        vibrator.cancel()
    }

    override fun hasVibrator(): Boolean {
        return vibrator.hasVibrator()
    }
}
