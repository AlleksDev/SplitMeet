package com.coditos.splitmeet.features.detailOuting.domain.strategies

import com.coditos.splitmeet.features.detailOuting.presentation.screens.DetailOutingUiState
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.CalculateSplitsRequest

interface CalculateSplitStrategy {
    /**
     * Validates if the calculate splits action can proceed.
     * Returns true if valid, false if an error was recorded in the ViewModel.
     */
    fun validate(state: DetailOutingUiState, onError: (String) -> Unit): Boolean

    /**
     * Extracts values for the backend DTO based on the current ui state.
     */
    fun extractRequestParams(state: DetailOutingUiState): CalculateSplitsRequest? = null
}

class EqualCalculateStrategy : CalculateSplitStrategy {
    override fun validate(state: DetailOutingUiState, onError: (String) -> Unit): Boolean {
        // Equal split usually doesn't need specific validation before calling calculate
        if (state.participants.isEmpty()) {
            onError("Se necesitan participantes para dividir equitativamente")
            return false
        }
        return true
    }
}

class CustomFixedCalculateStrategy : CalculateSplitStrategy {
    override fun validate(state: DetailOutingUiState, onError: (String) -> Unit): Boolean {
        // En custom_fixed, el cálculo se basa en que existan items y que sus splits coincidan
        if (state.items.isEmpty()) {
            onError("Se requiere agregar al menos un consumo para especificar montos fijos")
            return false
        }
        return true
    }
}

class PerConsumptionCalculateStrategy : CalculateSplitStrategy {
    override fun validate(state: DetailOutingUiState, onError: (String) -> Unit): Boolean {
        if (state.items.isEmpty()) {
            onError("Se requiere agregar consumos para calcular por consumo")
            return false
        }
        return true
    }
}

class SinglePayerCalculateStrategy : CalculateSplitStrategy {
    override fun validate(state: DetailOutingUiState, onError: (String) -> Unit): Boolean {
        if (state.selectedSinglePayerId == null) {
            onError("Debes seleccionar un responsable que asumirá la cuenta")
            return false
        }
        return true
    }

    override fun extractRequestParams(state: DetailOutingUiState): CalculateSplitsRequest? {
        return CalculateSplitsRequest(singlePayerId = state.selectedSinglePayerId)
    }
}
