package com.coditos.splitmeet.core.session.domain.repositories

import com.coditos.splitmeet.core.session.domain.model.SessionValidationStatus

interface SessionRepository {
    suspend fun validateSession(): SessionValidationStatus
}
