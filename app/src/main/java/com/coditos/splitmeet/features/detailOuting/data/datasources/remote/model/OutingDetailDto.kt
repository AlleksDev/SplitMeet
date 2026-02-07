package com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class OutingDetailDto(
    @SerializedName(value = "ID", alternate = ["id", "Id"])
    val id: Long? = null,
    @SerializedName(value = "Name", alternate = ["name"])
    val name: String? = null,
    @SerializedName(value = "Description", alternate = ["description"])
    val description: String? = null,
    @SerializedName(value = "CategoryID", alternate = ["category_id", "categoryId"])
    val categoryId: Long? = null,
    @SerializedName(value = "CategoryName", alternate = ["category_name", "categoryName"])
    val categoryName: String? = null,
    @SerializedName(value = "GroupID", alternate = ["group_id", "groupId"])
    val groupId: Long? = null,
    @SerializedName(value = "CreatorID", alternate = ["creator_id", "creatorId"])
    val creatorId: Long? = null,
    @SerializedName(value = "OutingDate", alternate = ["outing_date", "outingDate"])
    val outingDate: String? = null,
    @SerializedName(value = "SplitType", alternate = ["split_type", "splitType"])
    val splitType: String? = null,
    @SerializedName(value = "TotalAmount", alternate = ["total_amount", "totalAmount"])
    val totalAmount: Double? = null,
    @SerializedName(value = "Status", alternate = ["status"])
    val status: String? = null,
    @SerializedName(value = "IsEditable", alternate = ["is_editable", "isEditable"])
    val isEditable: Boolean? = null,
    @SerializedName(value = "ParticipantCount", alternate = ["participant_count", "participantCount"])
    val participantCount: Int? = null,
    @SerializedName(value = "PaidCount", alternate = ["paid_count", "paidCount"])
    val paidCount: Int? = null,
    @SerializedName(value = "CreatedAt", alternate = ["created_at", "createdAt"])
    val createdAt: String? = null,
    @SerializedName(value = "UpdatedAt", alternate = ["updated_at", "updatedAt"])
    val updatedAt: String? = null
)
