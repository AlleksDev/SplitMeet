package com.coditos.splitmeet.features.home.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class OutingDto(
    @SerializedName(value = "ID", alternate = ["id", "Id"])
    val id: Long,
    @SerializedName(value = "Name", alternate = ["name"])
    val name: String,
    @SerializedName(value = "Description", alternate = ["description"])
    val description: String?,
    @SerializedName(value = "CategoryName", alternate = ["categoryName", "category_name"])
    val categoryName: String,
    @SerializedName(value = "SplitType", alternate = ["splitType", "split_type"])
    val splitType: String,
    @SerializedName(value = "TotalAmount", alternate = ["totalAmount", "total_amount"])
    val totalAmount: Float,
    @SerializedName(value = "ParticipantCount", alternate = ["participantCount", "participant_count"])
    val participantCount: Int,
    @SerializedName(value = "PaidCount", alternate = ["paidCount", "paid_count"])
    val paidCount: Int
)
