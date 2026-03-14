package com.coditos.splitmeet.features.detailOuting.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.features.detailOuting.domain.usecases.AddParticipantUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.ConfirmPaymentUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.DeleteOutingUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetCategoriesUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetOutingDetailUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetOutingItemsUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.GetParticipantsUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.RemoveParticipantUseCase
import com.coditos.splitmeet.features.detailOuting.domain.usecases.SearchUsersUseCase
import com.coditos.splitmeet.features.detailOuting.domain.entities.Participant
import com.coditos.splitmeet.core.hardware.domain.FingerPrintManager
import com.coditos.splitmeet.features.detailOuting.domain.usecases.DetailOutingUseCases
import com.coditos.splitmeet.features.profile.domain.usecases.GetProfileUseCase
import com.coditos.splitmeet.features.detailOuting.presentation.screens.DetailOutingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class DetailOutingViewModel @Inject constructor(
    private val useCases: DetailOutingUseCases,
    private val getProfileUseCase: GetProfileUseCase,
    private val fingerPrintManager: FingerPrintManager
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
            val detailResult = useCases.getOutingDetail(outingId)
            Log.d("DetailOutingViewModel", "Detail result: $detailResult")

            detailResult.fold(
                onSuccess = { detail ->
                    _uiState.update { it.copy(outingDetail = detail) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
            )

            // Check if current user is the creator
            val profileResult = getProfileUseCase()
            profileResult.onSuccess { profile ->
                val isCreator = _uiState.value.outingDetail?.creatorId == profile.id
                _uiState.update { it.copy(isCreator = isCreator) }
            }

            // Load participants
            loadParticipants()

            // Load items
            loadItems()

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun loadParticipants() {
        _uiState.update { it.copy(isParticipantsLoading = true) }

        val participantsResult = useCases.getParticipants(outingId)
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

        val itemsResult = useCases.getOutingItems(outingId)
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

        val result = useCases.searchUsers(query)
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
            val result = useCases.addParticipant(outingId, userId)
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

    fun confirmPayment(participant: Participant) {
        if (!participant.isPaymentPending) return

        if (!fingerPrintManager.hasFingerPrint()) {
            _uiState.update {
                it.copy(error = "Tu dispositivo no cuenta con hardware biométrico.")
            }
            return
        }

        if (!fingerPrintManager.hasEnrolledFingerPrints()) {
            _uiState.update {
                it.copy(error = "No tienes huellas registradas. Configura la biometría en los ajustes de tu dispositivo.")
            }
            return
        }

        _uiState.update { it.copy(requireBiometricAuth = participant) }
    }

    fun onBiometricAuthDismissed() {
        _uiState.update { it.copy(requireBiometricAuth = null) }
        }

        fun onBiometricAuthError(errorMessage: String) {
            _uiState.update {
                it.copy(
                    requireBiometricAuth = null,
                    error = "Error biométrico: $errorMessage"
                )
            }
    }

    fun onBiometricAuthFailed() {
        _uiState.update {
            it.copy(
                requireBiometricAuth = null,
                error = "Autenticación biométrica fallida."
            )
        }
    }

    fun executeConfirmPayment(participant: Participant) {
        val paymentId = participant.paymentId ?: participant.id
        _uiState.update { it.copy(confirmingPaymentUserId = participant.userId, error = null) }

        viewModelScope.launch {
            val result = useCases.confirmPayment(paymentId)

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(confirmingPaymentUserId = null, selectedParticipantId = null) }
                    loadParticipants()
                    showSuccessMessage("Pago confirmado para @${participant.username}")
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            confirmingPaymentUserId = null,
                            error = mapOperationError(error)
                        )
                    }
                }
            )
        }
    }

    fun requestRemoveParticipant(participant: Participant) {
        _uiState.update {
            it.copy(
                showRemoveParticipantDialog = true,
                participantToRemove = participant
            )
        }
    }

    fun dismissRemoveParticipantDialog() {
        if (_uiState.value.removingParticipantUserId != null) return
        _uiState.update {
            it.copy(
                showRemoveParticipantDialog = false,
                participantToRemove = null
            )
        }
    }

    fun removeSelectedParticipant() {
        val participant = _uiState.value.participantToRemove ?: return
        _uiState.update {
            it.copy(
                removingParticipantUserId = participant.userId,
                error = null
            )
        }

        viewModelScope.launch {
            val result = useCases.removeParticipant(outingId, participant.userId)

            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            removingParticipantUserId = null,
                            showRemoveParticipantDialog = false,
                            participantToRemove = null,
                            selectedParticipantId = null
                        )
                    }
                    loadParticipants()
                    showSuccessMessage("Participante eliminado")
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            removingParticipantUserId = null,
                            showRemoveParticipantDialog = false,
                            participantToRemove = null,
                            error = mapOperationError(error)
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
            val categoriesResult = useCases.getCategories()
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
            val result = useCases.updateOuting(
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
            val result = useCases.deleteOuting(outingId)

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

    // Participant selection functions
    fun selectParticipant(participantId: Long) {
        _uiState.update {
            if (it.selectedParticipantId == participantId) {
                it.copy(selectedParticipantId = null)
            } else {
                it.copy(selectedParticipantId = participantId)
            }
        }
    }

    fun clearParticipantSelection() {
        _uiState.update { it.copy(selectedParticipantId = null) }
    }

    private fun mapOperationError(error: Throwable): String {
        return when ((error as? HttpException)?.code()) {
            401 -> "Tu sesión expiró. Inicia sesión nuevamente."
            400 -> error.message ?: "No se pudo completar la operación."
            else -> error.message ?: "Ocurrió un error de red."
        }
    }
}
