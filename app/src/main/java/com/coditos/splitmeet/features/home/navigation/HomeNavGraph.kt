package com.coditos.splitmeet.features.home.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.coditos.splitmeet.core.hardware.data.AndroidQrScanner
import com.coditos.splitmeet.core.hardware.presentation.QrCameraScreen
import com.coditos.splitmeet.core.navigation.CreateGroup
import com.coditos.splitmeet.core.navigation.CreateOuting
import com.coditos.splitmeet.core.navigation.FeatureNavGraph
import com.coditos.splitmeet.core.navigation.GroupDetail
import com.coditos.splitmeet.core.navigation.Home
import com.coditos.splitmeet.core.navigation.Login
import com.coditos.splitmeet.core.navigation.MainScreen
import com.coditos.splitmeet.core.navigation.OutingDetail
import com.coditos.splitmeet.features.detailOuting.domain.usecases.DetailOutingUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeNavGraph @Inject constructor(
    private val qrScanner: AndroidQrScanner,
    private val detailOutingUseCases: DetailOutingUseCases
) : FeatureNavGraph {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Home> {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()

            val showCameraFlow = remember { MutableStateFlow(false) }
            val showCamera by showCameraFlow.collectAsStateWithLifecycle()

            val isJoiningFlow = remember { MutableStateFlow(false) }
            val isJoining by isJoiningFlow.collectAsStateWithLifecycle()

            val joinErrorFlow = remember { MutableStateFlow<String?>(null) }
            val joinError by joinErrorFlow.collectAsStateWithLifecycle()

            Box(modifier = Modifier.fillMaxSize()) {

                MainScreen(
                    onNavigateToCreateOuting = {
                        navController.navigate(CreateOuting)
                    },
                    onNavigateToCreateGroup = {
                        navController.navigate(CreateGroup)
                    },
                    onNavigateToOutingDetail = { outingId ->
                        navController.navigate(OutingDetail(outingId))
                    },
                    onNavigateToGroupDetail = { groupId ->
                        navController.navigate(GroupDetail(groupId))
                    },
                    onScanQrClick = {
                        showCameraFlow.update { true }
                    },
                    onLoggedOut = {
                        navController.navigate(Login) {
                            popUpTo(Home) { inclusive = true }
                        }
                    }
                )

                if (showCamera) {
                    QrCameraScreen(
                        qrScanner = qrScanner,
                        onQrDetected = { qrText ->
                            coroutineScope.launch {
                                handleQrScanned(
                                    qrText = qrText,
                                    navController = navController,
                                    context = context,
                                    showCameraFlow = showCameraFlow,
                                    isJoiningFlow = isJoiningFlow,
                                    joinErrorFlow = joinErrorFlow
                                )
                            }
                        },
                        onError = { error ->
                            showCameraFlow.update { false }
                            Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                        },
                        onNavigateBack = {
                            showCameraFlow.update { false }
                            joinErrorFlow.update { null }
                        },
                        isProcessing = isJoining,
                        errorMessage = joinError,
                        onRetryAfterError = {
                            joinErrorFlow.update { null }
                            isJoiningFlow.update { false }
                        }
                    )
                }
            }
        }
    }

    private suspend fun handleQrScanned(
        qrText: String,
        navController: NavHostController,
        context: android.content.Context,
        showCameraFlow: MutableStateFlow<Boolean>,
        isJoiningFlow: MutableStateFlow<Boolean>,
        joinErrorFlow: MutableStateFlow<String?>
    ) {
        try {
            // Parse the URL to extract the type and ID
            // Expected formats:
            // https://frimeet.fun/outings/{id}/join
            // https://frimeet.fun/groups/{id}/join

            when {
                qrText.contains("/outings/") && qrText.contains("/join") -> {
                    // Extract outing ID from URL
                    val outingId = qrText
                        .substringAfter("/outings/")
                        .substringBefore("/join")
                        .toLongOrNull()

                    if (outingId != null) {
                        // Start joining process
                        isJoiningFlow.update { true }
                        joinErrorFlow.update { null }

                        // Call the join API
                        val result = detailOutingUseCases.joinOuting(outingId)

                        result.onSuccess {
                            // Successfully joined, navigate to detail
                            showCameraFlow.update { false }
                            isJoiningFlow.update { false }
                            navController.navigate(OutingDetail(outingId, joinAutomatically = false)) {
                                popUpTo(Home) { inclusive = false }
                            }
                            Toast.makeText(context, "¡Te has unido a la salida!", Toast.LENGTH_SHORT).show()
                        }.onFailure { error ->
                            // Failed to join, show error
                            isJoiningFlow.update { false }
                            val errorMessage = when {
                                error.message?.contains("401") == true -> "Sesión expirada. Por favor inicia sesión nuevamente."
                                error.message?.contains("404") == true -> "La salida no existe."
                                error.message?.contains("409") == true -> "Ya eres un participante de esta salida."
                                else -> "Error al unirse a la salida: ${error.message}"
                            }
                            joinErrorFlow.update { errorMessage }
                        }
                    } else {
                        showCameraFlow.update { false }
                        Toast.makeText(context, "ID de salida inválido", Toast.LENGTH_SHORT).show()
                    }
                }
                qrText.contains("/groups/") && qrText.contains("/join") -> {
                    // Extract group ID from URL
                    val groupId = qrText
                        .substringAfter("/groups/")
                        .substringBefore("/join")
                        .toLongOrNull()

                    if (groupId != null) {
                        showCameraFlow.update { false }
                        navController.navigate(GroupDetail(groupId))
                    } else {
                        showCameraFlow.update { false }
                        Toast.makeText(context, "ID de grupo inválido", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    showCameraFlow.update { false }
                    Toast.makeText(context, "Código QR no reconocido", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            showCameraFlow.update { false }
            isJoiningFlow.update { false }
            joinErrorFlow.update { "Error al procesar QR: ${e.message}" }
        }
    }
}