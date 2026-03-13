package com.coditos.splitmeet.features.group.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coditos.splitmeet.features.group.presentation.screens.components.AddMemberButton
import com.coditos.splitmeet.features.group.presentation.screens.components.DeleteGroupDialog
import com.coditos.splitmeet.features.group.presentation.screens.components.EditGroupModal
import com.coditos.splitmeet.features.group.presentation.screens.components.GroupInfoHeader
import com.coditos.splitmeet.features.group.presentation.screens.components.InviteMemberDialog
import com.coditos.splitmeet.features.group.presentation.screens.components.MemberCard
import com.coditos.splitmeet.features.group.presentation.viewmodels.GroupDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(
    viewModel: GroupDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Grupos",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
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
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.primary,
                thickness = 2.dp
            )

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
                    val group = uiState.group!!

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            GroupInfoHeader(
                                group = group,
                                onEdit = { viewModel.showEditModal() },
                                onDelete = { viewModel.showDeleteDialog() }
                            )
                        }

                        if (group.description.isNotBlank()) {
                            item {
                                Text(
                                    text = group.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        item {
                            Text(
                                text = "${uiState.members.size} integrantes",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        items(uiState.members, key = { it.id }) { member ->
                            MemberCard(
                                member = member,
                                onRemove = { viewModel.removeMember(member.userId) }
                            )
                        }

                        item {
                            AddMemberButton(
                                onClick = { viewModel.showInviteDialog() }
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
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

