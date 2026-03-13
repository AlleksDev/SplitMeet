package com.coditos.splitmeet.core.session.domain.usecases

import com.coditos.splitmeet.core.session.domain.model.AppStartDestination
import com.coditos.splitmeet.core.session.domain.model.SessionValidationStatus
import com.coditos.splitmeet.core.session.domain.repositories.SessionRepository
import com.coditos.splitmeet.core.storage.TokenDataStore
import javax.inject.Inject

class ResolveStartDestinationUseCase @Inject constructor(
    private val tokenDataStore: TokenDataStore,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(): AppStartDestination {
        val token = tokenDataStore.getToken()

        if (token.isNullOrBlank()) {
            return AppStartDestination.LOGIN
        }

        return when (sessionRepository.validateSession()) {
            SessionValidationStatus.Valid -> AppStartDestination.HOME
            SessionValidationStatus.InvalidToken -> {
                tokenDataStore.clearToken()
                AppStartDestination.LOGIN
            }
            SessionValidationStatus.UnknownError -> AppStartDestination.HOME
        }
    }
}
