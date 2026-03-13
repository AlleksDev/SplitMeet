package com.coditos.splitmeet.core.session.data.repositories

import com.coditos.splitmeet.core.session.data.datasources.remote.api.SessionApi
import com.coditos.splitmeet.core.session.domain.model.SessionValidationStatus
import com.coditos.splitmeet.core.session.domain.repositories.SessionRepository
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepositoryImpl @Inject constructor(
    private val sessionApi: SessionApi
) : SessionRepository {
    override suspend fun validateSession(): SessionValidationStatus {
        return try {
            val response = sessionApi.validateSession()
            when {
                response.isSuccessful -> SessionValidationStatus.Valid
                response.code() in setOf(401, 403) -> SessionValidationStatus.InvalidToken
                else -> SessionValidationStatus.UnknownError
            }
        } catch (error: HttpException) {
            if (error.code() in setOf(401, 403)) {
                SessionValidationStatus.InvalidToken
            } else {
                SessionValidationStatus.UnknownError
            }
        } catch (_: Exception) {
            SessionValidationStatus.UnknownError
        }
    }
}
