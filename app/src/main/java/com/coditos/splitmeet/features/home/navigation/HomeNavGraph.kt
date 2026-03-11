package com.coditos.splitmeet.features.home.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.coditos.splitmeet.core.hardware.data.AndroidQrScanner
import com.coditos.splitmeet.core.hardware.presentation.QrCameraScreen
import com.coditos.splitmeet.core.navigation.CreateOuting
import com.coditos.splitmeet.core.navigation.FeatureNavGraph
import com.coditos.splitmeet.core.navigation.Home
import com.coditos.splitmeet.core.navigation.MainScreen
import com.coditos.splitmeet.core.navigation.OutingDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class HomeNavGraph @Inject constructor(
    private val qrScanner: AndroidQrScanner
) : FeatureNavGraph {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Home> {
            val context = LocalContext.current

            val showCameraFlow = remember { MutableStateFlow(false) }

            val showCamera by showCameraFlow.collectAsStateWithLifecycle()

            Box(modifier = Modifier.fillMaxSize()) {

                MainScreen(
                    onNavigateToCreateOuting = {
                        navController.navigate(CreateOuting)
                    },
                    onNavigateToOutingDetail = { outingId ->
                        navController.navigate(OutingDetail(outingId))
                    },
                    onScanQrClick = {
                        showCameraFlow.update { true }
                    }
                )

                if (showCamera) {
                    QrCameraScreen(
                        qrScanner = qrScanner,
                        onQrDetected = { qrText ->
                            showCameraFlow.update { false }

                            Toast.makeText(context, "QR Leído: $qrText", Toast.LENGTH_LONG).show()
                        },
                        onError = { error ->
                            showCameraFlow.update { false }
                            Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}