package com.coditos.splitmeet.features.profile.data.datasources.remote.api

import com.coditos.splitmeet.features.profile.data.datasources.remote.model.UpdateProfileRequest
import com.coditos.splitmeet.features.profile.data.datasources.remote.model.UpdateProfileResponse
import com.coditos.splitmeet.features.profile.data.datasources.remote.model.UserProfileDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface ProfileApi {

    @GET("users/profile")
    suspend fun getProfile(): UserProfileDto

    @PATCH("users/update")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequest
    ): UpdateProfileResponse
}
