package com.coditos.splitmeet.core.hardware.presentation

import android.view.ViewGroup
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.coditos.splitmeet.core.hardware.data.AndroidQrScanner

@Composable
fun QrCameraScreen(
    qrScanner: AndroidQrScanner,
    onQrDetected: (String) -> Unit,
    onError: (Exception) -> Unit,
    onNavigateBack: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        onDispose {
            qrScanner.stopScan()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {

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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
        )

        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 16.dp),
            shape = CircleShape,
            color = Color.White
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar al Inicio",
                    tint = Color.Black
                )
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp),
            shape = CircleShape,
            color = Color.Green
        ) {
            Box(modifier = Modifier.size(12.dp))
        }
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(250.dp)
        ) {
            ReticleCorners()
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 300.dp), // Padding debajo del retículo
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enfoque el código QR dentro del recuadro para escanear",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Esperando código...",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun ReticleCorners() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val cornerLineLength = 40.dp.toPx()
        val cornerLineThickness = 2.dp.toPx()
        val cornerLineColor = Color.White

        drawLine(
            color = cornerLineColor,
            start = Offset(0f, 0f),
            end = Offset(cornerLineLength, 0f),
            strokeWidth = cornerLineThickness
        )
        drawLine(
            color = cornerLineColor,
            start = Offset(0f, 0f),
            end = Offset(0f, cornerLineLength),
            strokeWidth = cornerLineThickness
        )

        drawLine(
            color = cornerLineColor,
            start = Offset(size.width, 0f),
            end = Offset(size.width - cornerLineLength, 0f),
            strokeWidth = cornerLineThickness
        )
        drawLine(
            color = cornerLineColor,
            start = Offset(size.width, 0f),
            end = Offset(size.width, cornerLineLength),
            strokeWidth = cornerLineThickness
        )

        drawLine(
            color = cornerLineColor,
            start = Offset(0f, size.height),
            end = Offset(cornerLineLength, size.height),
            strokeWidth = cornerLineThickness
        )
        drawLine(
            color = cornerLineColor,
            start = Offset(0f, size.height),
            end = Offset(0f, size.height - cornerLineLength),
            strokeWidth = cornerLineThickness
        )

        drawLine(
            color = cornerLineColor,
            start = Offset(size.width, size.height),
            end = Offset(size.width - cornerLineLength, size.height),
            strokeWidth = cornerLineThickness
        )
        drawLine(
            color = cornerLineColor,
            start = Offset(size.width, size.height),
            end = Offset(size.width, size.height - cornerLineLength),
            strokeWidth = cornerLineThickness
        )
    }
}