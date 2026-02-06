package com.coditos.splitmeet.features.product.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class CreateOutingItemRequest(
    @SerializedName("product_id")
    val productId: Long? = null,
    @SerializedName("custom_name")
    val customName: String? = null,
    @SerializedName("custom_presentation")
    val customPresentation: String? = null,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("unit_price")
    val unitPrice: Double,
    @SerializedName("is_shared")
    val isShared: Boolean = false
)
