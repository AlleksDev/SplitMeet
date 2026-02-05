package com.coditos.splitmeet.core.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Base ViewModel with common functionality for UI state management
 */
abstract class BaseViewModel<T> : ViewModel() {
    
    protected val _uiState = MutableStateFlow<UiState<T>>(UiState.idle())
    val uiState: StateFlow<UiState<T>> = _uiState.asStateFlow()
    
    protected fun setLoading() {
        _uiState.value = UiState.loading()
    }
    
    protected fun setSuccess(data: T) {
        _uiState.value = UiState.success(data)
    }
    
    protected fun setError(message: String) {
        _uiState.value = UiState.error(message)
    }
    
    protected fun resetState() {
        _uiState.value = UiState.idle()
    }
}
