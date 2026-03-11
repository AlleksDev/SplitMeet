package com.coditos.splitmeet.core.hardware.presentation

import android.view.ViewGroup
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.coditos.splitmeet.core.hardware.data.AndroidQrScanner

@Composable
fun QrCameraScreen(
    qrScanner: AndroidQrScanner,
    onQrDetected: (String) -> Unit,
    onError: (Exception) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        onDispose {
            qrScanner.stopScan()
        }
    }

    AndroidView(
        factory = { context ->
            PreviewView(context).apply {
                this.scaleType = PreviewView.ScaleType.FILL_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                qrScanner.bindToLifecycle(
                    lifecycleOwner = lifecycleOwner,
                    previewView = this,
                    onQrDetected = onQrDetected,
                    onError = onError
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}