package com.coditos.splitmeet.features.detailOuting.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.features.detailOuting.domain.usecases.DetailOutingUseCases
import com.coditos.splitmeet.features.detailOuting.presentation.screens.ShowOutingQrUiState
import com.coditos.splitmeet.features.detailOuting.util.OutingQrGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowOutingQrViewModel @Inject constructor(
    private val useCases: DetailOutingUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<ShowOutingQrUiState>(ShowOutingQrUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun generateQrCode(outingId: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.emit(ShowOutingQrUiState.Loading)

            val result = useCases.generateOutingQr(outingId)
            result.fold(
                onSuccess = { qrContent ->
                    try {
                        val bitmap = OutingQrGenerator.generateBitmap(qrContent.deepLink, sizePx = 512)
                        _uiState.emit(ShowOutingQrUiState.Success(bitmap))
                    } catch (e: Exception) {
                        _uiState.emit(ShowOutingQrUiState.Error("Error al generar el código QR: ${e.message}"))
                    }
                },
                onFailure = { throwable ->
                    _uiState.emit(ShowOutingQrUiState.Error("Error al generar el código QR: ${throwable.message}"))
                }
            )
        }
    }
}
