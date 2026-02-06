package com.coditos.splitmeet.features.outing.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class CategoryDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("is_active")
    val isActive: Boolean?,
    @SerializedName("created_at")
    val createdAt: String?
)
