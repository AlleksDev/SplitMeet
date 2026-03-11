package com.coditos.splitmeet.features.group.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.features.group.domain.usecases.GetGroupDetailUseCase
import com.coditos.splitmeet.features.group.domain.usecases.GetGroupMembersUseCase
import com.coditos.splitmeet.features.group.domain.usecases.InviteMemberUseCase
import com.coditos.splitmeet.features.group.domain.usecases.RemoveMemberUseCase
import com.coditos.splitmeet.features.group.presentation.screens.GroupDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getGroupDetailUseCase: GetGroupDetailUseCase,
    private val getGroupMembersUseCase: GetGroupMembersUseCase,
    private val inviteMemberUseCase: InviteMemberUseCase,
    private val removeMemberUseCase: RemoveMemberUseCase
) : ViewModel() {

    private val groupId: Long = savedStateHandle["groupId"] ?: 0L

    private val _uiState = MutableStateFlow(GroupDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadGroupDetail()
    }

    fun loadGroupDetail() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getGroupDetailUseCase(groupId).fold(
                onSuccess = { group ->
                    _uiState.update { it.copy(group = group) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                    return@launch
                }
            )
            getGroupMembersUseCase(groupId).fold(
                onSuccess = { members ->
                    _uiState.update { it.copy(isLoading = false, members = members) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    fun showInviteDialog() {
        _uiState.update { it.copy(showInviteDialog = true) }
    }

    fun dismissInviteDialog() {
        _uiState.update { it.copy(showInviteDialog = false) }
    }

    fun inviteMember(username: String) {
        _uiState.update { it.copy(isInviting = true) }
        viewModelScope.launch {
            inviteMemberUseCase(groupId, username).fold(
                onSuccess = {
                    _uiState.update { it.copy(isInviting = false, showInviteDialog = false) }
                    loadGroupDetail()
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isInviting = false, error = e.message) }
                }
            )
        }
    }

    fun removeMember(userId: Long) {
        viewModelScope.launch {
            removeMemberUseCase(groupId, userId).fold(
                onSuccess = { loadGroupDetail() },
                onFailure = { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
            )
        }
    }
}
