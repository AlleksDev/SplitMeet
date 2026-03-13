package com.coditos.splitmeet.features.profile.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(
    @SerializedName("message") val message: String,
    @SerializedName("user") val user: UserProfileDto
)
