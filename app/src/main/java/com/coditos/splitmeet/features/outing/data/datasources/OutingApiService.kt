package com.coditos.splitmeet.features.outing.data.datasources

import com.coditos.splitmeet.features.outing.data.datasources.dto.CreateOutingRequestDto
import com.coditos.splitmeet.features.outing.data.datasources.dto.OutingDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Retrofit API service for Outing feature
 */
interface OutingApiService {
    
    @GET("outings")
    suspend fun getOutings(): List<OutingDto>
    
    @GET("outings/active")
    suspend fun getActiveOutings(): List<OutingDto>
    
    @GET("outings/history")
    suspend fun getOutingHistory(): List<OutingDto>
    
    @GET("outings/{id}")
    suspend fun getOutingById(@Path("id") id: String): OutingDto
    
    @POST("outings")
    suspend fun createOuting(@Body request: CreateOutingRequestDto): OutingDto
    
    @PUT("outings/{id}/close")
    suspend fun closeOuting(@Path("id") id: String): OutingDto
    
    @DELETE("outings/{id}")
    suspend fun deleteOuting(@Path("id") id: String)
}
