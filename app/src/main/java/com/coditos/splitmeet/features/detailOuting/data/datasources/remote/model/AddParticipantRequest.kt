package com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class AddParticipantRequest(
    @SerializedName("user_id")
    val userId: Long
)
