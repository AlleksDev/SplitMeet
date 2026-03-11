package com.coditos.splitmeet.core.hardware.domain

interface FingerPrintManager {
    fun hasFingerPrint(): Boolean
    fun hasEnrolledFingerPrints(): Boolean
    fun authenticate(
        onSuccess: () -> Unit,
        onError: (errorCode: Int, errorMessage: String) -> Unit,
        onFailed: () -> Unit
    )
}