package com.coditos.splitmeet.features.home.data.datasources.remote.api

import com.coditos.splitmeet.features.home.data.datasources.remote.model.OutingDto
import retrofit2.http.GET

interface OutingApi {
    @GET("outings/me")
    suspend fun getOutings(): List<OutingDto>
}