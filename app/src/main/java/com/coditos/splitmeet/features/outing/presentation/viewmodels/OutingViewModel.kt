package com.coditos.splitmeet.features.outing.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.features.outing.data.datasources.remote.model.CreateOutingRequest
import com.coditos.splitmeet.features.outing.domain.entities.Category
import com.coditos.splitmeet.features.outing.domain.entities.SplitType
import com.coditos.splitmeet.features.outing.domain.usecases.CreateOutingUseCase
import com.coditos.splitmeet.features.outing.domain.usecases.GetCategoriesUseCase
import com.coditos.splitmeet.features.outing.presentation.screens.CreateOutingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OutingViewModel(
    private val createOutingUseCase: CreateOutingUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateOutingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        _uiState.update { it.copy(isCategoriesLoading = true) }

        viewModelScope.launch {
            val result = getCategoriesUseCase()
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
                            error = error.message
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
            val request = CreateOutingRequest(
                name = currentState.name,
                description = currentState.description.ifBlank { null },
                categoryId = currentState.selectedCategory!!.id,
                outingDate = currentState.selectedDate,
                splitType = currentState.selectedSplitType!!.value
            )

            Log.d("OutingViewModel", "Creating outing with request: $request")

            val result = createOutingUseCase(request)

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
                            error = error.message ?: "Error al crear la salida"
                        )
                    }
                )
            }
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
