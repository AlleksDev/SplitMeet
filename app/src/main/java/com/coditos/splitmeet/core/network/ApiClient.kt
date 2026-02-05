package com.coditos.splitmeet.core.network

import com.coditos.splitmeet.features.home.data.datasources.remote.model.OutingDto
import retrofit2.http.GET
import retrofit2.http.Path

interface SplitMeetApi {
    @GET("home/items")
    suspend fun getOutings(): List<OutingDto>

    @GET("home/items/{id}")
    suspend fun getOutingById(@Path("id") id: String): OutingDto
}