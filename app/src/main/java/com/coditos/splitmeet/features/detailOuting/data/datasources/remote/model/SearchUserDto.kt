package com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class SearchUserDto(
    @SerializedName(value = "id", alternate = ["ID"])
    val id: Long? = null,
    @SerializedName(value = "username", alternate = ["Username"])
    val username: String? = null,
    @SerializedName(value = "name", alternate = ["Name"])
    val name: String? = null,
    @SerializedName(value = "email", alternate = ["Email"])
    val email: String? = null,
    @SerializedName(value = "phone", alternate = ["Phone"])
    val phone: String? = null
)
