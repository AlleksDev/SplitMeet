package com.coditos.splitmeet.features.detailOuting.presentation.screens

import android.graphics.Bitmap

sealed interface ShowOutingQrUiState {
    data object Loading : ShowOutingQrUiState
    data class Success(val bitmap: Bitmap) : ShowOutingQrUiState
    data class Error(val message: String) : ShowOutingQrUiState
}
