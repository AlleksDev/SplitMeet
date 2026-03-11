package com.coditos.splitmeet.features.detailOuting.data.datasources.remote.api

import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.AddParticipantRequest
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.AddParticipantResponse
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.OutingDetailDto
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.OutingItemDto
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.ParticipantDto
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.SearchUserDto
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.UpdateOutingRequest
import com.coditos.splitmeet.features.outing.data.datasources.remote.model.CategoryDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DetailOutingApi {

    @GET("outings/{id}")
    suspend fun getOutingById(@Path("id") id: Long): OutingDetailDto

    @PATCH("outings/{id}")
    suspend fun updateOuting(
        @Path("id") id: Long,
        @Body request: UpdateOutingRequest
    ): OutingDetailDto

    @DELETE("outings/{id}")
    suspend fun deleteOuting(@Path("id") id: Long)

    @GET("outings/{id}/participants")
    suspend fun getParticipants(@Path("id") outingId: Long): List<ParticipantDto>

    @POST("outings/{id}/participants")
    suspend fun addParticipant(
        @Path("id") outingId: Long,
        @Body request: AddParticipantRequest
    ): AddParticipantResponse

    @GET("outings/{id}/items")
    suspend fun getOutingItems(@Path("id") outingId: Long): List<OutingItemDto>

    @GET("users/search")
    suspend fun searchUsers(@Query("username") username: String): List<SearchUserDto>

    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>
}
