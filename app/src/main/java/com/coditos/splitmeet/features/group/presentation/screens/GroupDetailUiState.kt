package com.coditos.splitmeet.features.group.presentation.screens

import com.coditos.splitmeet.features.detailOuting.domain.entities.SearchUser
import com.coditos.splitmeet.features.group.domain.entities.Group
import com.coditos.splitmeet.features.group.domain.entities.GroupMember

data class GroupDetailUiState(
    // Loading / data
    val isLoading: Boolean = false,
    val group: Group? = null,
    val members: List<GroupMember> = emptyList(),
    val error: String? = null,
    // Invite / search
    val showInviteDialog: Boolean = false,
    val isInviting: Boolean = false,
    val invitingUserId: Long? = null,
    val memberRemoved: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<SearchUser> = emptyList(),
    val isSearching: Boolean = false,
    val searchError: String? = null,
    // Edit
    val showEditModal: Boolean = false,
    val isUpdating: Boolean = false,
    val editName: String = "",
    val editDescription: String = "",
    // Delete
    val showDeleteDialog: Boolean = false,
    val isDeleting: Boolean = false
)
