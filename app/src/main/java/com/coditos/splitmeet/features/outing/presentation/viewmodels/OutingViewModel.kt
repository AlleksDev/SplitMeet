package com.coditos.splitmeet.features.outing.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.features.outing.data.datasources.remote.model.CreateOutingRequest
import com.coditos.splitmeet.features.outing.domain.entities.Category
import com.coditos.splitmeet.features.outing.domain.entities.SplitType
import com.coditos.splitmeet.features.outing.domain.usecases.OutingUseCases
import com.coditos.splitmeet.features.group.domain.repositories.GroupRepository
import com.coditos.splitmeet.features.group.domain.entities.Group
import com.coditos.splitmeet.features.outing.presentation.screens.CreateOutingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OutingViewModel @Inject constructor(
    private val useCases: OutingUseCases,
    private val groupRepository: GroupRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateOutingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        Log.d("OutingViewModel", "OutingViewModel initialized")
        loadCategories()
        loadGroups()
    }

    private fun loadCategories() {
        _uiState.update { it.copy(isCategoriesLoading = true) }

        viewModelScope.launch {
            val result = useCases.getCategories()
            Log.d("OutingViewModel", "Categories result: $result")

            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { categories ->
                        currentState.copy(
                            isCategoriesLoading = false,
                            categories = categories
                        )
                    },
                    onFailure = { error ->
                        currentState.copy(
                            isCategoriesLoading = false,
                            error = getFriendlyErrorMessage(error)
                        )
                    }
                )
            }
        }
    }

    private fun loadGroups() {
        _uiState.update { it.copy(isGroupsLoading = true) }

        viewModelScope.launch {
            val result = groupRepository.getMyGroups()
            Log.d("OutingViewModel", "Groups result: $result")

            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { groups ->
                      
                        // Set the default option as selected by default
                        currentState.copy(
                            isGroupsLoading = false,
                            groups = groups,
                            selectedGroup = groups.firstOrNull()
                        )
                    },
                    onFailure = { error ->
                        currentState.copy(
                            isGroupsLoading = false,
                            groupError = getFriendlyErrorMessage(error)
                        )
                    }
                )
            }
        }
    }

    fun onNameChanged(name: String) {
        _uiState.update { 
            it.copy(
                name = name,
                nameError = null
            ) 
        }
    }

    fun onDescriptionChanged(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onDateSelected(date: String) {
        _uiState.update { 
            it.copy(
                selectedDate = date,
                dateError = null
            ) 
        }
    }

    fun onCategorySelected(category: Category) {
        _uiState.update { 
            it.copy(
                selectedCategory = category,
                categoryError = null
            ) 
        }
    }

    fun onSplitTypeSelected(splitType: SplitType) {
        _uiState.update { 
            it.copy(
                selectedSplitType = splitType,
                splitTypeError = null
            ) 
        }
    }

    fun onGroupSelected(group: Group) {
        _uiState.update {
            it.copy(
                selectedGroup = group,
                groupError = null
            )
        }
    }

    fun createOuting() {
        val currentState = _uiState.value

        // Validate form
        var hasErrors = false
        var updatedState = currentState

        if (currentState.name.isBlank()) {
            updatedState = updatedState.copy(nameError = "El nombre es requerido")
            hasErrors = true
        }

        if (currentState.selectedDate.isBlank()) {
            updatedState = updatedState.copy(dateError = "La fecha es requerida")
            hasErrors = true
        }

        if (currentState.selectedCategory == null) {
            updatedState = updatedState.copy(categoryError = "Selecciona un tipo de salida")
            hasErrors = true
        }

        if (currentState.selectedSplitType == null) {
            updatedState = updatedState.copy(splitTypeError = "Selecciona un tipo de cálculo")
            hasErrors = true
        }

        if (hasErrors) {
            _uiState.update { updatedState }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val payloadGroupId = if (currentState.selectedGroup?.id == -1L) null else currentState.selectedGroup?.id

            val request = CreateOutingRequest(
                name = currentState.name,
                description = currentState.description.ifBlank { null },
                categoryId = currentState.selectedCategory!!.id,
                outingDate = currentState.selectedDate,
                splitType = currentState.selectedSplitType!!.value,
                groupId = payloadGroupId
            )

            Log.d("OutingViewModel", "Creating outing with request: $request")

            val result = useCases.createOuting(request)

            _uiState.update { state ->
                result.fold(
                    onSuccess = { createdOuting ->
                        Log.d("OutingViewModel", "Outing created: $createdOuting")
                        state.copy(
                            isLoading = false,
                            isSuccess = true,
                            createdOutingId = createdOuting.id
                        )
                    },
                    onFailure = { error ->
                        Log.e("OutingViewModel", "Error creating outing", error)
                        state.copy(
                            isLoading = false,
                            error = getFriendlyErrorMessage(error)
                        )
                    }
                )
            }
        }
    }

    private fun getFriendlyErrorMessage(error: Throwable?): String {
        val msg = error?.message?.lowercase() ?: return "Ocurrió un error inesperado. Por favor, inténtalo de nuevo."

        return when {
            msg.contains("401") || msg.contains("unauthorized") -> "Tu sesión ha expirado. Por favor, vuelve a iniciar sesión."
            msg.contains("400") || msg.contains("bad request") -> "Verifica que los datos ingresados sean correctos."
            msg.contains("404") -> "No pudimos encontrar la información solicitada."
            msg.contains("timeout") -> "La conexión tardó demasiado. Revisa tu internet e inténtalo de nuevo."
            msg.contains("network") || msg.contains("unknownhost") || msg.contains("connect") -> "No hay conexión a internet. Revisa tu red."
            msg.contains("500") || msg.contains("internal") -> "Problemas con el servidor. Por favor, inténtalo más tarde."
            else -> "No pudimos guardar los cambios. Inténtalo de nuevo."
        }
    }

    fun clearState() {
        _uiState.update { 
            CreateOutingUiState(
                categories = it.categories,
                splitTypes = it.splitTypes
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
