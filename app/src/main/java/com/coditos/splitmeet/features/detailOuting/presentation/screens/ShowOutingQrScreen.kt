package com.coditos.splitmeet.features.detailOuting.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.Text as ComposeText
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.ui.text.style.TextAlign
import com.coditos.splitmeet.features.detailOuting.presentation.viewmodels.ShowOutingQrViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowOutingQrScreen(
    outingId: Long,
    viewModel: ShowOutingQrViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(outingId) {
        viewModel.generateQrCode(outingId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        ComposeText("Código QR")
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorScheme.primary,
                        titleContentColor = colorScheme.onPrimary,
                        navigationIconContentColor = colorScheme.onPrimary
                    )
                )
            },
            containerColor = colorScheme.background
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when (uiState) {
                    is ShowOutingQrUiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(56.dp),
                            color = colorScheme.primary
                        )
                    }

                    is ShowOutingQrUiState.Success -> {
                        val bitmap = (uiState as ShowOutingQrUiState.Success).bitmap
                        androidx.compose.foundation.Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Código QR",
                            modifier = Modifier
                                .size(280.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(colorScheme.surface)
                        )
                    }

                    is ShowOutingQrUiState.Error -> {
                        val errorMessage = (uiState as ShowOutingQrUiState.Error).message
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ComposeText(
                                text = "Error al generar el código QR",
                                style = MaterialTheme.typography.headlineSmall,
                                color = colorScheme.error
                            )
                            androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(8.dp))
                            ComposeText(
                                text = errorMessage,
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
