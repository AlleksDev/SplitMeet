package com.coditos.splitmeet.features.group.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coditos.splitmeet.core.ui.components.SplitMeetTopBar
import com.coditos.splitmeet.features.group.presentation.components.GroupDetailContent
import com.coditos.splitmeet.features.group.presentation.screens.components.DeleteGroupDialog
import com.coditos.splitmeet.features.group.presentation.screens.components.EditGroupModal
import com.coditos.splitmeet.features.group.presentation.screens.components.InviteMemberDialog
import com.coditos.splitmeet.features.group.presentation.viewmodels.GroupDetailViewModel

@Composable
fun GroupDetailScreen(
    viewModel: GroupDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            SplitMeetTopBar(
                title = "Grupos",
                showLogo = false,
                showBackButton = true,
                onBackClick = onNavigateBack
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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

                uiState.group != null -> {
                    GroupDetailContent(
                        group = uiState.group!!,
                        members = uiState.members,
                        onEditGroup = { viewModel.showEditModal() },
                        onDeleteGroup = { viewModel.showDeleteDialog() },
                        onRemoveMember = { viewModel.removeMember(it) },
                        onInviteMember = { viewModel.showInviteDialog() }
                    )
                }
            }
        }
    }

    // Invite member bottom sheet
    if (uiState.showInviteDialog) {
        InviteMemberDialog(
            searchQuery = uiState.searchQuery,
            searchResults = uiState.searchResults,
            isSearching = uiState.isSearching,
            searchError = uiState.searchError,
            invitingUserId = uiState.invitingUserId,
            onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
            onInvite = { userId, username -> viewModel.inviteMemberById(userId, username) },
            onDismiss = { viewModel.dismissInviteDialog() }
        )
    }

    // Edit group bottom sheet
    if (uiState.showEditModal) {
        EditGroupModal(
            name = uiState.editName,
            description = uiState.editDescription,
            isUpdating = uiState.isUpdating,
            onNameChange = { viewModel.onEditNameChanged(it) },
            onDescriptionChange = { viewModel.onEditDescriptionChanged(it) },
            onSave = { viewModel.updateGroup() },
            onDismiss = { viewModel.dismissEditModal() }
        )
    }

    // Delete confirmation dialog
    if (uiState.showDeleteDialog) {
        DeleteGroupDialog(
            groupName = uiState.group?.name ?: "",
            isDeleting = uiState.isDeleting,
            onConfirm = { viewModel.deleteGroup(onDeleted = onNavigateBack) },
            onDismiss = { viewModel.dismissDeleteDialog() }
        )
    }
}

