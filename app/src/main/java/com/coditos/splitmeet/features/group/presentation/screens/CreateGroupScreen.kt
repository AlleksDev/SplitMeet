package com.coditos.splitmeet.features.group.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coditos.splitmeet.core.ui.components.SplitMeetTopBar
import com.coditos.splitmeet.features.group.presentation.viewmodels.CreateGroupViewModel
import com.coditos.splitmeet.features.outing.presentation.components.OutingButton
import com.coditos.splitmeet.features.outing.presentation.components.OutingDescriptionField
import com.coditos.splitmeet.features.outing.presentation.components.OutingTextField

@Composable
fun CreateGroupScreen(
    viewModel: CreateGroupViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onGroupCreated: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    // Handle success
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onGroupCreated()
            viewModel.clearState()
        }
    }

    // Handle error
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SplitMeetTopBar(
                title = "Nuevo grupo",
                showLogo = false,
                showBackButton = true,
                onBackClick = onNavigateBack
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                        .imePadding(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    OutingTextField(
                        value = uiState.name,
                        onValueChange = viewModel::onNameChanged,
                        label = "Nombre del grupo",
                        placeholder = "Ej. Roommates",
                        isError = uiState.nameError != null,
                        errorMessage = uiState.nameError,
                        enabled = !uiState.isLoading
                    )

                    OutingDescriptionField(
                        value = uiState.description,
                        onValueChange = viewModel::onDescriptionChanged,
                        label = "Descripción",
                        placeholder = "Ej. Grupo para dividir gastos del departamento",
                        enabled = !uiState.isLoading
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    OutingButton(
                        text = "Crear grupo",
                        onClick = viewModel::createGroup,
                        isLoading = uiState.isLoading,
                        enabled = uiState.isFormValid
                    )
                }
            }
        }
    }
}
