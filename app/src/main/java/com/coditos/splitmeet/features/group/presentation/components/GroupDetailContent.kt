package com.coditos.splitmeet.features.group.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.coditos.splitmeet.features.group.domain.entities.Group
import com.coditos.splitmeet.features.group.domain.entities.GroupMember
import com.coditos.splitmeet.features.group.presentation.screens.components.AddMemberButton
import com.coditos.splitmeet.features.group.presentation.screens.components.GroupInfoHeader
import com.coditos.splitmeet.features.group.presentation.screens.components.MemberCard

@Composable
fun GroupDetailContent(
    group: Group,
    members: List<GroupMember>,
    onEditGroup: () -> Unit,
    onDeleteGroup: () -> Unit,
    onRemoveMember: (Long) -> Unit,
    onInviteMember: () -> Unit
) {
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
                onEdit = onEditGroup,
                onDelete = onDeleteGroup
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
                text = "${members.size} integrantes",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(members, key = { it.id }) { member ->
            MemberCard(
                member = member,
                onRemove = { onRemoveMember(member.userId) }
            )
        }

        item {
            AddMemberButton(onClick = onInviteMember)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
