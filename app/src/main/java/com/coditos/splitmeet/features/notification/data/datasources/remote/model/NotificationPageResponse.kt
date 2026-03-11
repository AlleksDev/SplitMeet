package com.coditos.splitmeet.features.notification.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class NotificationPageResponse(
    @SerializedName("data") val data: List<NotificationDto>,
    @SerializedName("page") val page: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("total") val total: Int
)
