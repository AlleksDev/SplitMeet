package com.coditos.splitmeet.features.notification.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class RegisterDeviceTokenRequest(
    @SerializedName("token") val token: String,
    @SerializedName("platform") val platform: String = "android"
)
