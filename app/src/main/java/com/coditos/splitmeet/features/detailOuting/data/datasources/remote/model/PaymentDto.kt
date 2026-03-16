package com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class PaymentDto(
    @SerializedName(value = "ID", alternate = ["id"])
    val id: Long? = null,
    @SerializedName(value = "OutingID", alternate = ["outing_id", "outingId"])
    val outingId: Long? = null,
    @SerializedName(value = "ParticipantID", alternate = ["participant_id", "participantId"])
    val participantId: Long? = null,
    @SerializedName(value = "Amount", alternate = ["amount"])
    val amount: Double? = null,
    @SerializedName(value = "Status", alternate = ["status"])
    val status: String? = null,
    @SerializedName(value = "PaidAt", alternate = ["paid_at", "paidAt"])
    val paidAt: String? = null,
    @SerializedName(value = "ConfirmedBy", alternate = ["confirmed_by", "confirmedBy"])
    val confirmedBy: Long? = null,
    @SerializedName(value = "Notes", alternate = ["notes"])
    val notes: String? = null,
    @SerializedName(value = "CreatedAt", alternate = ["created_at", "createdAt"])
    val createdAt: String? = null,
    @SerializedName(value = "UpdatedAt", alternate = ["updated_at", "updatedAt"])
    val updatedAt: String? = null,
    @SerializedName(value = "ParticipantUsername", alternate = ["participant_username", "participantUsername"])
    val participantUsername: String? = null,
    @SerializedName(value = "ParticipantName", alternate = ["participant_name", "participantName"])
    val participantName: String? = null,
    @SerializedName(value = "ConfirmedByUsername", alternate = ["confirmed_by_username", "confirmedByUsername"])
    val confirmedByUsername: String? = null
)
