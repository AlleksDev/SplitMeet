package com.coditos.splitmeet.core.hardware.domain

interface QrScanner {
    fun startScan(
        onQrDetected: (String) -> Unit,
        onError: (Exception) -> Unit
    )
    fun stopScan()
}
