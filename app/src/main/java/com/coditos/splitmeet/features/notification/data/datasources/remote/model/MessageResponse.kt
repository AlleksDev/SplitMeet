package com.coditos.splitmeet.features.notification.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("message") val message: String
)
