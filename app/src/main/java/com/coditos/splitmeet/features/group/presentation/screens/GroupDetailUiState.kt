package com.coditos.splitmeet.features.group.presentation.screens

import com.coditos.splitmeet.features.group.domain.entities.Group
import com.coditos.splitmeet.features.group.domain.entities.GroupMember

data class GroupDetailUiState(
    val isLoading: Boolean = false,
    val group: Group? = null,
    val members: List<GroupMember> = emptyList(),
    val error: String? = null,
    val showInviteDialog: Boolean = false,
    val isInviting: Boolean = false,
    val memberRemoved: Boolean = false
)
