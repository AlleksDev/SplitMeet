package com.coditos.splitmeet.features.notification.domain.usecases

import com.coditos.splitmeet.features.notification.domain.entities.Notification
import com.coditos.splitmeet.features.notification.domain.repositories.NotificationRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class ObserveNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): SharedFlow<Notification> = repository.notifications
}
