package com.coditos.splitmeet.features.group.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.features.detailOuting.domain.usecases.SearchUsersUseCase
import com.coditos.splitmeet.features.group.domain.usecases.GroupUseCases
import com.coditos.splitmeet.features.group.presentation.screens.GroupDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val groupUseCases: GroupUseCases,
    private val searchUsersUseCase: SearchUsersUseCase
) : ViewModel() {

    private val groupId: Long = savedStateHandle["groupId"] ?: 0L

    private val _uiState = MutableStateFlow(GroupDetailUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadGroupDetail()
    }

    fun loadGroupDetail() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            groupUseCases.getGroupDetail(groupId).fold(
                onSuccess = { group ->
                    _uiState.update { it.copy(group = group) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                    return@launch
                }
            )
            groupUseCases.getGroupMembers(groupId).fold(
                onSuccess = { members ->
                    _uiState.update { it.copy(isLoading = false, members = members) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    // ── Invite / Search ──────────────────────────────────────────────────────

    fun showInviteDialog() {
        _uiState.update {
            it.copy(
                showInviteDialog = true,
                searchQuery = "",
                searchResults = emptyList(),
                searchError = null,
                invitingUserId = null
            )
        }
    }

    fun dismissInviteDialog() {
        _uiState.update {
            it.copy(
                showInviteDialog = false,
                searchQuery = "",
                searchResults = emptyList(),
                searchError = null,
                invitingUserId = null
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            if (query.isBlank()) {
                _uiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
                return@launch
            }
            _uiState.update { it.copy(isSearching = true, searchError = null) }
            searchUsersUseCase(query).fold(
                onSuccess = { users ->
                    val existingUserIds = _uiState.value.members.map { it.userId }.toSet()
                    _uiState.update {
                        it.copy(
                            searchResults = users.filter { u -> u.id !in existingUserIds },
                            isSearching = false
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isSearching = false, searchError = e.message) }
                }
            )
        }
    }

    fun inviteMember(username: String) {
        _uiState.update { it.copy(isInviting = true) }
        viewModelScope.launch {
            groupUseCases.inviteMember(groupId, username).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isInviting = false,
                            showInviteDialog = false,
                            searchQuery = "",
                            searchResults = emptyList()
                        )
                    }
                    loadGroupDetail()
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isInviting = false, searchError = e.message) }
                }
            )
        }
    }

    fun inviteMemberById(userId: Long, username: String) {
        _uiState.update { it.copy(invitingUserId = userId, searchError = null) }
        viewModelScope.launch {
            groupUseCases.inviteMember(groupId, username).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            invitingUserId = null,
                            showInviteDialog = false,
                            searchQuery = "",
                            searchResults = emptyList()
                        )
                    }
                    loadGroupDetail()
                },
                onFailure = { e ->
                    _uiState.update { it.copy(invitingUserId = null, searchError = e.message) }
                }
            )
        }
    }

    fun removeMember(userId: Long) {
        viewModelScope.launch {
            groupUseCases.removeMember(groupId, userId).fold(
                onSuccess = { loadGroupDetail() },
                onFailure = { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
            )
        }
    }

    // ── Delete ───────────────────────────────────────────────────────────────

    fun showDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = true) }
    }

    fun dismissDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun deleteGroup(onDeleted: () -> Unit) {
        _uiState.update { it.copy(isDeleting = true) }
        viewModelScope.launch {
            groupUseCases.deleteGroup(groupId).fold(
                onSuccess = {
                    _uiState.update { it.copy(isDeleting = false, showDeleteDialog = false) }
                    onDeleted()
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isDeleting = false, error = e.message) }
                }
            )
        }
    }

    // ── Edit ─────────────────────────────────────────────────────────────────

    fun showEditModal() {
        val group = _uiState.value.group ?: return
        _uiState.update {
            it.copy(
                showEditModal = true,
                editName = group.name,
                editDescription = group.description
            )
        }
    }

    fun dismissEditModal() {
        _uiState.update { it.copy(showEditModal = false) }
    }

    fun onEditNameChanged(name: String) {
        _uiState.update { it.copy(editName = name) }
    }

    fun onEditDescriptionChanged(description: String) {
        _uiState.update { it.copy(editDescription = description) }
    }

    fun updateGroup() {
        val name = _uiState.value.editName.trim()
        if (name.isBlank()) return
        _uiState.update { it.copy(isUpdating = true) }
        viewModelScope.launch {
            groupUseCases.updateGroup(groupId, name, _uiState.value.editDescription.trim()).fold(
                onSuccess = { updatedGroup ->
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            showEditModal = false,
                            group = updatedGroup
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isUpdating = false, error = e.message) }
                }
            )
        }
    }
}
