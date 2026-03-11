package com.coditos.splitmeet.features.notification.domain.usecases

import com.coditos.splitmeet.features.notification.domain.entities.SseConnectionState
import com.coditos.splitmeet.features.notification.domain.repositories.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSseConnectionUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<SseConnectionState> = repository.connectionState
}
