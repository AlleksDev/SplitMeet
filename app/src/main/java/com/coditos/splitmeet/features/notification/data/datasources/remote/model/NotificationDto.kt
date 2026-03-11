package com.coditos.splitmeet.features.notification.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class NotificationDto(
    @SerializedName("id") val id: Long,
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String,
    @SerializedName("reference_id") val referenceId: Long?,
    @SerializedName("inviter_name") val inviterName: String?,
    @SerializedName("group_name") val groupName: String?,
    @SerializedName("outing_name") val outingName: String?,
    @SerializedName("is_read") val isRead: Boolean,
    @SerializedName("created_at") val createdAt: String
)
