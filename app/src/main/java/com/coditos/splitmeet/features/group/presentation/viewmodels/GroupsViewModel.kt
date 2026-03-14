package com.coditos.splitmeet.features.group.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.features.group.domain.usecases.GroupUseCases
import com.coditos.splitmeet.features.group.presentation.screens.GroupsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor(
    private val groupUseCases: GroupUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadGroups()
    }

    fun loadGroups() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            groupUseCases.getMyGroups().fold(
                onSuccess = { groups ->
                    _uiState.update { it.copy(isLoading = false, groups = groups) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    fun deleteGroup(groupId: Long) {
        viewModelScope.launch {
            groupUseCases.deleteGroup(groupId).fold(
                onSuccess = { loadGroups() },
                onFailure = { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
            )
        }
    }
}
