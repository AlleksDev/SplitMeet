package com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class ParticipantDto(
    @SerializedName(value = "ID", alternate = ["id"])
    val id: Long? = null,
    @SerializedName(value = "OutingID", alternate = ["outing_id", "outingId"])
    val outingId: Long? = null,
    @SerializedName(value = "UserID", alternate = ["user_id", "userId"])
    val userId: Long? = null,
    @SerializedName(value = "Username", alternate = ["username"])
    val username: String? = null,
    @SerializedName(value = "Name", alternate = ["name"])
    val name: String? = null,
    @SerializedName(value = "Status", alternate = ["status"])
    val status: String? = null,
    @SerializedName(value = "AmountOwed", alternate = ["amount_owed", "amountOwed"])
    val amountOwed: Double? = null,
    @SerializedName(value = "CustomAmount", alternate = ["custom_amount", "customAmount"])
    val customAmount: Double? = null,
    @SerializedName(value = "PaymentStatus", alternate = ["payment_status", "paymentStatus"])
    val paymentStatus: String? = null,
    @SerializedName(value = "JoinedAt", alternate = ["joined_at", "joinedAt"])
    val joinedAt: String? = null
)
