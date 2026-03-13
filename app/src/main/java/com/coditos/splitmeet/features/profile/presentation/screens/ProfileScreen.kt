package com.coditos.splitmeet.features.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coditos.splitmeet.features.profile.presentation.components.EditProfileDialog
import com.coditos.splitmeet.features.profile.presentation.components.ProfileContent
import com.coditos.splitmeet.features.profile.presentation.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLoggedOut: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.loggedOut) {
        if (uiState.loggedOut) onLoggedOut()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.error!!,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            uiState.profile != null -> {
                ProfileContent(
                    profile = uiState.profile!!,
                    onLogout = { viewModel.logout() },
                    onEdit = { viewModel.showEditDialog() }
                )
            }
        }
    }

    if (uiState.showEditDialog) {
        EditProfileDialog(
            name = uiState.editName,
            phone = uiState.editPhone,
            password = uiState.editPassword,
            isUpdating = uiState.isUpdating,
            onNameChange = { viewModel.onEditNameChanged(it) },
            onPhoneChange = { viewModel.onEditPhoneChanged(it) },
            onPasswordChange = { viewModel.onEditPasswordChanged(it) },
            onDismiss = { viewModel.dismissEditDialog() },
            onSave = { viewModel.updateProfile() }
        )
    }
}
