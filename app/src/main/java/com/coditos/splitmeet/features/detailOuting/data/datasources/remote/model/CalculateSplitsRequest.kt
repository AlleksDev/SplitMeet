package com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class CalculateSplitsRequest(
    @SerializedName("single_payer_id")
    val singlePayerId: Long? = null
)
