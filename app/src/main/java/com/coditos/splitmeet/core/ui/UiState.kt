package com.coditos.splitmeet.core.ui

/**
 * Represents the UI state for screens
 */
data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String? = null
) {
    companion object {
        fun <T> loading(): UiState<T> = UiState(isLoading = true)
        fun <T> success(data: T): UiState<T> = UiState(data = data)
        fun <T> error(message: String): UiState<T> = UiState(error = message)
        fun <T> idle(): UiState<T> = UiState()
    }
    
    val isSuccess: Boolean get() = data != null && !isLoading && error == null
    val isError: Boolean get() = error != null && !isLoading
    val isIdle: Boolean get() = !isLoading && data == null && error == null
}

/**
 * Extension function to convert NetworkResult to UiState
 */
fun <T> NetworkResult<T>.toUiState(): UiState<T> = when (this) {
    is NetworkResult.Loading -> UiState.loading()
    is NetworkResult.Success -> UiState.success(data)
    is NetworkResult.Error -> UiState.error(message)
}
