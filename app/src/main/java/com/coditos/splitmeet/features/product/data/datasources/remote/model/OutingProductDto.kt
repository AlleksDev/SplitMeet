package com.coditos.splitmeet.features.product.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class OutingProductDto(
    @SerializedName(value = "id", alternate = ["ID"])
    val id: Long? = null,
    @SerializedName(value = "outing_id", alternate = ["OutingID", "outingId"])
    val outingId: Long? = null,
    @SerializedName(value = "product_id", alternate = ["ProductID", "productId"])
    val productId: Long? = null,
    @SerializedName(value = "product_name", alternate = ["ProductName", "productName"])
    val productName: String? = null,
    @SerializedName(value = "custom_name", alternate = ["CustomName", "customName"])
    val customName: String? = null,
    @SerializedName(value = "custom_presentation", alternate = ["CustomPresentation", "customPresentation"])
    val customPresentation: String? = null,
    @SerializedName(value = "presentation", alternate = ["Presentation", "ProductPresentation"])
    val presentation: String? = null,
    @SerializedName(value = "size", alternate = ["Size", "ProductSize"])
    val size: String? = null,
    @SerializedName(value = "quantity", alternate = ["Quantity"])
    val quantity: Int? = null,
    @SerializedName(value = "unit_price", alternate = ["UnitPrice", "unitPrice"])
    val unitPrice: Double? = null,
    @SerializedName(value = "subtotal", alternate = ["Subtotal"])
    val subtotal: Double? = null,
    @SerializedName(value = "is_shared", alternate = ["IsShared", "isShared"])
    val isShared: Boolean? = null,
    @SerializedName(value = "created_at", alternate = ["CreatedAt", "createdAt"])
    val createdAt: String? = null
)
