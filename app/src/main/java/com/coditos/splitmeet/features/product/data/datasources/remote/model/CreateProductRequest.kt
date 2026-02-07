package com.coditos.splitmeet.features.product.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class CreateProductRequest(
    @SerializedName("category_id")
    val categoryId: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("presentation")
    val presentation: String? = null,
    @SerializedName("default_price")
    val defaultPrice: Double? = null
)
