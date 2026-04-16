package com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class CalculateSplitsResponseDto(
    @SerializedName("outing_id")
    val outingId: Long,
    @SerializedName("total_amount")
    val totalAmount: Double,
    @SerializedName("split_type")
    val splitType: String,
    @SerializedName("user_summaries")
    val userSummaries: List<UserSummaryDto>
)

data class UserSummaryDto(
    @SerializedName("participant_id")
    val participantId: Long,
    @SerializedName("user_id")
    val userId: Long,
    @SerializedName("username")
    val username: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("total_owed")
    val totalOwed: Double,
    @SerializedName("items_count")
    val itemsCount: Int
)
