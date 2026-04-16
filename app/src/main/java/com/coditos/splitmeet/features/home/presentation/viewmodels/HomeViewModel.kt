package com.coditos.splitmeet.features.home.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.features.home.domain.usecases.HomeUseCases
import com.coditos.splitmeet.features.home.presentation.screens.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getOutings()    // 1. Observa Room como Flow continuo
        syncOutings()   // 2. Sincroniza con API en paralelo
    }

    private fun getOutings() {
        homeUseCases.getOutings()
            .onEach { outings ->
                Log.d("HomeViewModel", "Room emitió: ${outings.size} outings")
                val active = outings.filter { it.status == "active" }
                val history = outings.filter { it.status == "completed" || it.status == "cancelled" }
                _uiState.update { it.copy(activeOutings = active, historyOutings = history, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    fun syncOutings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true, error = null) }
            try {
                homeUseCases.syncOutings()
            } catch (e: Exception) {
                // Solo muestra error si además no hay datos en caché
                if (_uiState.value.activeOutings.isEmpty() && _uiState.value.historyOutings.isEmpty()) {
                    _uiState.update {
                        it.copy(error = "Sin conexión y sin datos guardados")
                    }
                }
                // Si hay datos en caché, falla silenciosamente
            } finally {
                _uiState.update { it.copy(isSyncing = false) }
            }
        }
    }

    fun onRefresh() {
        syncOutings()
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }
}