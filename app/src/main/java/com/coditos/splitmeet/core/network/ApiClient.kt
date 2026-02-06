package com.coditos.splitmeet.core.network

import com.coditos.splitmeet.features.home.data.datasources.remote.model.OutingDto
import retrofit2.http.GET
import retrofit2.http.Path

interface SplitMeetApi {
    @GET("outings/me")
    suspend fun getOutings(): List<OutingDto>

    @GET("outings/{id}")
    suspend fun getOutingById(@Path("id") id: String): OutingDto
}