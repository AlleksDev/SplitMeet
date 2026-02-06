package com.coditos.splitmeet.features.detailOuting.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.features.detailOuting.domain.usecases.AddParticipantUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.DeleteOutingUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetOutingDetailUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetOutingItemsUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetParticipantsUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.SearchUsersUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.UpdateOutingUseCase
import com.coditos.splitmeet.features.detailOuting.presentation.screens.DetailOutingUiState
import com.coditos.splitmeet.features.outing.domain.entities.Category
import com.coditos.splitmeet.features.outing.domain.usecases.GetCategoriesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailOutingViewModel(
    private val getOutingDetailUseCase: GetOutingDetailUseCase,
    private val getParticipantsUseCase: GetParticipantsUseCase,
    private val getOutingItemsUseCase: GetOutingItemsUseCase,
    private val searchUsersUseCase: SearchUsersUseCase,
    private val addParticipantUseCase: AddParticipantUseCase,
    private val updateOutingUseCase: UpdateOutingUseCase,
    private val deleteOutingUseCase: DeleteOutingUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailOutingUiState())
    val uiState = _uiState.asStateFlow()

    private var outingId: Long = 0
    private var searchJob: Job? = null
    
    private var onDeleteSuccess: (() -> Unit)? = null

    fun loadOutingDetail(outingId: Long) {
        this.outingId = outingId
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            // Load outing detail
            val detailResult = getOutingDetailUseCase(outingId)
            Log.d("DetailOutingViewModel", "Detail result: $detailResult")

            detailResult.fold(
                onSuccess = { detail ->
                    _uiState.update { it.copy(outingDetail = detail) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
            )

            // Load participants
            loadParticipants()

            // Load items
            loadItems()

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun loadParticipants() {
        _uiState.update { it.copy(isParticipantsLoading = true) }

        val participantsResult = getParticipantsUseCase(outingId)
        Log.d("DetailOutingViewModel", "Participants result: $participantsResult")

        participantsResult.fold(
            onSuccess = { participants ->
                _uiState.update { it.copy(participants = participants, isParticipantsLoading = false) }
            },
            onFailure = { error ->
                Log.e("DetailOutingViewModel", "Error loading participants", error)
                _uiState.update { it.copy(isParticipantsLoading = false) }
            }
        )
    }

    private suspend fun loadItems() {
        _uiState.update { it.copy(isItemsLoading = true) }

        val itemsResult = getOutingItemsUseCase(outingId)
        Log.d("DetailOutingViewModel", "Items result: $itemsResult")

        itemsResult.fold(
            onSuccess = { items ->
                _uiState.update { it.copy(items = items, isItemsLoading = false) }
            },
            onFailure = { error ->
                Log.e("DetailOutingViewModel", "Error loading items", error)
                _uiState.update { it.copy(isItemsLoading = false) }
            }
        )
    }

    fun refreshData() {
        viewModelScope.launch {
            loadParticipants()
            loadItems()
        }
    }

    // Add participant modal
    fun showAddParticipantModal() {
        _uiState.update { 
            it.copy(
                showAddParticipantModal = true,
                searchQuery = "",
                searchResults = emptyList(),
                addParticipantError = null,
                addingParticipantId = null
            ) 
        }
    }

    fun hideAddParticipantModal() {
        _uiState.update { 
            it.copy(
                showAddParticipantModal = false,
                searchQuery = "",
                searchResults = emptyList()
            ) 
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        
        // Debounce search
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // Wait 300ms before searching
            searchUsers(query)
        }
    }

    private suspend fun searchUsers(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }

        _uiState.update { it.copy(isSearching = true) }

        val result = searchUsersUseCase(query)
        Log.d("DetailOutingViewModel", "Search users result: $result")

        result.fold(
            onSuccess = { users ->
                // Filter out users who are already participants
                val existingUserIds = _uiState.value.participants.map { it.userId }.toSet()
                val filteredUsers = users.filter { it.id !in existingUserIds }
                _uiState.update { it.copy(searchResults = filteredUsers, isSearching = false) }
            },
            onFailure = { error ->
                Log.e("DetailOutingViewModel", "Error searching users", error)
                _uiState.update { it.copy(isSearching = false) }
            }
        )
    }

    fun addParticipant(userId: Long) {
        val user = _uiState.value.searchResults.find { it.id == userId }
        _uiState.update { it.copy(addingParticipantId = userId, addParticipantError = null) }

        viewModelScope.launch {
            val result = addParticipantUseCase(outingId, userId)
            Log.d("DetailOutingViewModel", "Add participant result: $result")

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(addingParticipantId = null) }
                    // Refresh participants list
                    loadParticipants()
                    // Show success message
                    showSuccessMessage("Invitación enviada a @${user?.username ?: "usuario"}")
                    // Hide modal after short delay
                    delay(500)
                    hideAddParticipantModal()
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            addingParticipantId = null,
                            addParticipantError = error.message ?: "Error al agregar participante"
                        ) 
                    }
                }
            )
        }
    }

    // Edit outing functions
    fun showEditModal() {
        val currentDetail = _uiState.value.outingDetail ?: return
        
        viewModelScope.launch {
            // Load categories first
            val categoriesResult = getCategoriesUseCase()
            val categories = categoriesResult.getOrDefault(emptyList())
            
            _uiState.update {
                it.copy(
                    showEditModal = true,
                    editName = currentDetail.name,
                    editDescription = currentDetail.description ?: "",
                    editCategoryId = currentDetail.categoryId,
                    editOutingDate = currentDetail.outingDate,
                    editSplitType = currentDetail.splitType,
                    categories = categories
                )
            }
        }
    }

    fun hideEditModal() {
        _uiState.update { it.copy(showEditModal = false) }
    }

    fun onEditNameChanged(name: String) {
        _uiState.update { it.copy(editName = name) }
    }

    fun onEditDescriptionChanged(description: String) {
        _uiState.update { it.copy(editDescription = description) }
    }

    fun onEditCategoryChanged(categoryId: Long) {
        _uiState.update { it.copy(editCategoryId = categoryId) }
    }

    fun onEditDateChanged(date: String) {
        _uiState.update { it.copy(editOutingDate = date) }
    }

    fun onEditSplitTypeChanged(splitType: String) {
        _uiState.update { it.copy(editSplitType = splitType) }
    }

    fun updateOuting() {
        val state = _uiState.value
        val categoryId = state.editCategoryId ?: return
        
        _uiState.update { it.copy(isUpdating = true) }

        viewModelScope.launch {
            val result = updateOutingUseCase(
                outingId = outingId,
                name = state.editName,
                description = state.editDescription.ifBlank { null },
                categoryId = categoryId,
                outingDate = state.editOutingDate,
                splitType = state.editSplitType
            )

            result.fold(
                onSuccess = { updatedDetail ->
                    _uiState.update { 
                        it.copy(
                            outingDetail = updatedDetail,
                            isUpdating = false,
                            showEditModal = false
                        ) 
                    }
                    showSuccessMessage("Salida actualizada con éxito")
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            isUpdating = false,
                            error = error.message ?: "Error al actualizar la salida"
                        ) 
                    }
                }
            )
        }
    }

    // Delete outing functions
    fun showDeleteConfirmation() {
        _uiState.update { it.copy(showDeleteConfirmation = true) }
    }

    fun hideDeleteConfirmation() {
        _uiState.update { it.copy(showDeleteConfirmation = false) }
    }

    fun setOnDeleteSuccess(callback: () -> Unit) {
        onDeleteSuccess = callback
    }

    fun deleteOuting() {
        _uiState.update { it.copy(isDeleting = true) }

        viewModelScope.launch {
            val result = deleteOutingUseCase(outingId)

            result.fold(
                onSuccess = {
                    _uiState.update { 
                        it.copy(
                            isDeleting = false,
                            showDeleteConfirmation = false
                        ) 
                    }
                    onDeleteSuccess?.invoke()
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            isDeleting = false,
                            showDeleteConfirmation = false,
                            error = error.message ?: "Error al eliminar la salida"
                        ) 
                    }
                }
            )
        }
    }

    // Success message functions
    private fun showSuccessMessage(message: String) {
        _uiState.update { 
            it.copy(
                successMessage = message,
                showSuccessMessage = true
            ) 
        }
        viewModelScope.launch {
            delay(3000)
            hideSuccessMessage()
        }
    }

    fun hideSuccessMessage() {
        _uiState.update { 
            it.copy(
                successMessage = null,
                showSuccessMessage = false
            ) 
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null, addParticipantError = null) }
    }
}
