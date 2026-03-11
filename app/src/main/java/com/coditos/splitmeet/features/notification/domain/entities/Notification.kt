package com.coditos.splitmeet.features.notification.domain.entities

data class Notification(
    val id: Long,
    val type: NotificationType,
    val title: String,
    val message: String,
    val referenceId: Long?,
    val inviterName: String?,
    val groupName: String?,
    val outingName: String?,
    val isRead: Boolean,
    val createdAt: String
) {
    /** True when the user can still accept or reject this notification. */
    val isPending: Boolean
        get() = type == NotificationType.GROUP_INVITATION ||
                type == NotificationType.OUTING_INVITATION
}
