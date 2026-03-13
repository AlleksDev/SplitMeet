package com.coditos.splitmeet.features.outing.data.datasources.remote.api

import com.coditos.splitmeet.features.outing.data.datasources.remote.model.CategoryDto
import com.coditos.splitmeet.features.outing.data.datasources.remote.model.CreateOutingRequest
import com.coditos.splitmeet.features.outing.data.datasources.remote.model.CreateOutingResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OutingApi {
    @POST("outings")
    suspend fun createOuting(
        @Body request: CreateOutingRequest
    ): CreateOutingResponse

    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>
}
