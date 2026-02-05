package com.coditos.splitmeet.features.outing.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.core.ui.BaseViewModel
import com.coditos.splitmeet.features.outing.domain.entities.Outing
import com.coditos.splitmeet.features.outing.domain.usecases.CreateOutingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for creating a new Outing
 */
class CreateOutingViewModel(
    private val createOutingUseCase: CreateOutingUseCase
) : BaseViewModel<Outing>() {

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category.asStateFlow()

    private val _totalAmount = MutableStateFlow("")
    val totalAmount: StateFlow<String> = _totalAmount.asStateFlow()

    private val _participantIds = MutableStateFlow<List<String>>(emptyList())
    val participantIds: StateFlow<List<String>> = _participantIds.asStateFlow()

    fun onTitleChange(value: String) {
        _title.value = value
    }

    fun onCategoryChange(value: String) {
        _category.value = value
    }

    fun onTotalAmountChange(value: String) {
        _totalAmount.value = value
    }

    fun onParticipantIdsChange(ids: List<String>) {
        _participantIds.value = ids
    }

    fun addParticipant(id: String) {
        _participantIds.value = _participantIds.value + id
    }

    fun removeParticipant(id: String) {
        _participantIds.value = _participantIds.value - id
    }

    fun createOuting() {
        viewModelScope.launch {
            setLoading()
            val amount = _totalAmount.value.toDoubleOrNull() ?: 0.0
            when (val result = createOutingUseCase(
                _title.value,
                _category.value,
                amount,
                _participantIds.value
            )) {
                is NetworkResult.Success -> setSuccess(result.data)
                is NetworkResult.Error -> setError(result.message)
                is NetworkResult.Loading -> setLoading()
            }
        }
    }

    fun resetForm() {
        _title.value = ""
        _category.value = ""
        _totalAmount.value = ""
        _participantIds.value = emptyList()
        resetState()
    }
}
