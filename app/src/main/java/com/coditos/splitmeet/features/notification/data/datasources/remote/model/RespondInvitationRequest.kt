package com.coditos.splitmeet.features.notification.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class RespondInvitationRequest(
    @SerializedName("accept") val accept: Boolean
)
