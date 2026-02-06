package com.coditos.splitmeet.features.outing.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class CreateOutingResponse(
    @SerializedName(value = "id", alternate = ["ID"])
    val id: Long? = null,
    @SerializedName(value = "name", alternate = ["Name"])
    val name: String? = null,
    @SerializedName(value = "description", alternate = ["Description"])
    val description: String? = null,
    @SerializedName(value = "category_id", alternate = ["CategoryID", "categoryId"])
    val categoryId: Long? = null,
    @SerializedName(value = "group_id", alternate = ["GroupID", "groupId"])
    val groupId: Long? = null,
    @SerializedName(value = "creator_id", alternate = ["CreatorID", "creatorId"])
    val creatorId: Long? = null,
    @SerializedName(value = "outing_date", alternate = ["OutingDate", "outingDate"])
    val outingDate: String? = null,
    @SerializedName(value = "split_type", alternate = ["SplitType", "splitType"])
    val splitType: String? = null,
    @SerializedName(value = "total_amount", alternate = ["TotalAmount", "totalAmount"])
    val totalAmount: Double? = null,
    @SerializedName(value = "status", alternate = ["Status"])
    val status: String? = null,
    @SerializedName(value = "is_editable", alternate = ["IsEditable", "isEditable"])
    val isEditable: Boolean? = null,
    @SerializedName(value = "created_at", alternate = ["CreatedAt", "createdAt"])
    val createdAt: String? = null,
    @SerializedName(value = "updated_at", alternate = ["UpdatedAt", "updatedAt"])
    val updatedAt: String? = null
)
