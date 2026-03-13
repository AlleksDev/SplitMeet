package com.coditos.splitmeet.core.session.domain.model

sealed interface SessionValidationStatus {
    data object Valid : SessionValidationStatus
    data object InvalidToken : SessionValidationStatus
    data object UnknownError : SessionValidationStatus
}
