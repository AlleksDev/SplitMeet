package com.coditos.splitmeet.features.notification.domain.usecases

import com.coditos.splitmeet.features.notification.domain.entities.Notification
import com.coditos.splitmeet.features.notification.domain.repositories.NotificationRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(page: Int, limit: Int): Result<List<Notification>> =
        repository.getNotifications(page, limit)
}
