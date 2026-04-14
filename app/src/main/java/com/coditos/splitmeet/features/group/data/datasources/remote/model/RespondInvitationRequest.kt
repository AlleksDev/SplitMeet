package com.coditos.splitmeet.features.group.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class RespondInvitationRequest(
    @SerializedName("accept")
    val accept: Boolean
)
