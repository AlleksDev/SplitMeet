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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coditos.splitmeet.features.group.presentation.screens.components.AddMemberButton
import com.coditos.splitmeet.features.group.presentation.screens.components.GroupDetailHeader
import com.coditos.splitmeet.features.group.presentation.screens.components.GroupInfoHeader
import com.coditos.splitmeet.features.group.presentation.screens.components.InviteMemberDialog
import com.coditos.splitmeet.features.group.presentation.screens.components.MemberCard
import com.coditos.splitmeet.features.group.presentation.viewmodels.GroupDetailViewModel

@Composable
fun GroupDetailScreen(
    viewModel: GroupDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        GroupDetailHeader(
            title = "Grupos",
            onNavigateBack = onNavigateBack
        )

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
                            onEdit = { /* TODO: edit */ },
                            onDelete = onNavigateBack
                        )
                    }

                    if (group.description.isNotBlank()) {
                        item {
                            Text(
                                text = group.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }

                    item {
                        Text(
                            text = "${uiState.members.size} integrantes",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFFFF9500)
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

    if (uiState.showInviteDialog) {
        InviteMemberDialog(
            isInviting = uiState.isInviting,
            onDismiss = { viewModel.dismissInviteDialog() },
            onInvite = { username -> viewModel.inviteMember(username) }
        )
    }
}
