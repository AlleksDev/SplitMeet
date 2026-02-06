package com.coditos.splitmeet.features.product.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName(value = "id", alternate = ["ID"])
    val id: Long? = null,
    @SerializedName(value = "category_id", alternate = ["CategoryID", "categoryId"])
    val categoryId: Long? = null,
    @SerializedName(value = "name", alternate = ["Name"])
    val name: String? = null,
    @SerializedName(value = "presentation", alternate = ["Presentation"])
    val presentation: String? = null,
    @SerializedName(value = "size", alternate = ["Size"])
    val size: String? = null,
    @SerializedName(value = "default_price", alternate = ["DefaultPrice", "defaultPrice"])
    val defaultPrice: Double? = null,
    @SerializedName(value = "is_predefined", alternate = ["IsPredefined", "isPredefined"])
    val isPredefined: Boolean? = null,
    @SerializedName(value = "created_by", alternate = ["CreatedBy", "createdBy"])
    val createdBy: Long? = null,
    @SerializedName(value = "created_at", alternate = ["CreatedAt", "createdAt"])
    val createdAt: String? = null
)
