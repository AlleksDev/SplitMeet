package com.coditos.splitmeet.features.notification.domain.usecases

import com.coditos.splitmeet.features.notification.domain.repositories.NotificationRepository
import javax.inject.Inject

class ConnectSseUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke() = repository.connect()
}
