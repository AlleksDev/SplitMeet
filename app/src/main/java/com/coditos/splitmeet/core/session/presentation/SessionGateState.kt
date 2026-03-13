package com.coditos.splitmeet.core.session.presentation

import com.coditos.splitmeet.core.session.domain.model.AppStartDestination

sealed interface SessionGateState {
    data object Loading : SessionGateState
    data class Ready(val destination: AppStartDestination) : SessionGateState
}
