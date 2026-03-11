package com.coditos.splitmeet.features.notification.domain.entities

enum class NotificationType(val value: String) {
    GROUP_INVITATION("group_invitation"),
    OUTING_INVITATION("outing_invitation"),
    INVITATION_ACCEPTED("invitation_accepted"),
    INVITATION_REJECTED("invitation_rejected"),
    UNKNOWN("unknown");

    companion object {
        fun fromValue(value: String): NotificationType =
            entries.find { it.value == value } ?: UNKNOWN
    }
}
