package com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class AddParticipantResponse(
    @SerializedName(value = "id", alternate = ["ID"])
    val id: Long? = null,
    @SerializedName(value = "outing_id", alternate = ["OutingID", "outingId"])
    val outingId: Long? = null,
    @SerializedName(value = "user_id", alternate = ["UserID", "userId"])
    val userId: Long? = null,
    @SerializedName(value = "status", alternate = ["Status"])
    val status: String? = null,
    @SerializedName(value = "message", alternate = ["Message"])
    val message: String? = null
)
