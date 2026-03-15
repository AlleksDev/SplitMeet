package com.coditos.splitmeet.core.hardware.domain

import androidx.fragment.app.FragmentActivity
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

interface FingerPrintManager {
    fun hasFingerPrint(): Boolean
    fun hasEnrolledFingerPrints(): Boolean
    fun authenticate(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (errorCode: Int, errorMessage: String) -> Unit,
        onFailed: () -> Unit
    )
}