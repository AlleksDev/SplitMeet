package com.coditos.splitmeet.features.notification.data.datasources.remote.mapper

import com.coditos.splitmeet.features.notification.data.datasources.remote.model.NotificationDto
import com.coditos.splitmeet.features.notification.domain.entities.Notification
import com.coditos.splitmeet.features.notification.domain.entities.NotificationType

fun NotificationDto.toDomain(): Notification = Notification(
    id = id,
    type = NotificationType.fromValue(type),
    title = title,
    message = message,
    referenceId = referenceId,
    inviterName = inviterName,
    groupName = groupName,
    outingName = outingName,
    isRead = isRead,
    createdAt = createdAt
)
