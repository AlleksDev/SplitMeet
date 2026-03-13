package com.coditos.splitmeet.features.notification.domain.entities

enum class ResponseStatus(val value: String) {
    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected");

    companion object {
        fun fromValue(value: String?): ResponseStatus =
            entries.find { it.value == value } ?: PENDING
    }
}

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
    val responseStatus: ResponseStatus,
    val createdAt: String
) {
    val isPending: Boolean
        get() = (type == NotificationType.GROUP_INVITATION ||
                type == NotificationType.OUTING_INVITATION) &&
                responseStatus == ResponseStatus.PENDING
    
    val isAccepted: Boolean
        get() = responseStatus == ResponseStatus.ACCEPTED
    
    val isRejected: Boolean
        get() = responseStatus == ResponseStatus.REJECTED
}
