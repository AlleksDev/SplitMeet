package com.coditos.splitmeet.features.profile.data.datasources.remote.api

import com.coditos.splitmeet.features.profile.data.datasources.remote.model.UserProfileDto
import retrofit2.http.GET

interface ProfileApi {

    @GET("users/profile")
    suspend fun getProfile(): UserProfileDto
}
