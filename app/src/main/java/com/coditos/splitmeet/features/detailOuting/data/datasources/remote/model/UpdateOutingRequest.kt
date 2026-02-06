package com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class UpdateOutingRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("category_id")
    val categoryId: Long,
    @SerializedName("outing_date")
    val outingDate: String,
    @SerializedName("split_type")
    val splitType: String
)
