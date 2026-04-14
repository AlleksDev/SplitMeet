package com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class ConfirmParticipationRequest(
    @SerializedName("accept")
    val accept: Boolean
)
