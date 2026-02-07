package com.coditos.splitmeet.core.network

import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.LoginRequest
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.LoginResponseDto
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.User
import com.coditos.splitmeet.features.auth.domain.entities.RegisterResponse
import com.coditos.splitmeet.features.home.data.datasources.remote.model.OutingDto
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.AddParticipantRequest
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.AddParticipantResponse
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.UpdateOutingRequest
import com.coditos.splitmeet.features.outing.data.datasources.remote.model.CategoryDto
import com.coditos.splitmeet.features.outing.data.datasources.remote.model.CreateOutingRequest
import com.coditos.splitmeet.features.outing.data.datasources.remote.model.CreateOutingResponse
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.OutingDetailDto
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.OutingItemDto
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.ParticipantDto
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.SearchUserDto
import com.coditos.splitmeet.features.product.data.datasources.remote.model.CreateOutingItemRequest
import com.coditos.splitmeet.features.product.data.datasources.remote.model.CreateProductRequest
import com.coditos.splitmeet.features.product.data.datasources.remote.model.OutingProductDto
import com.coditos.splitmeet.features.product.data.datasources.remote.model.ProductDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SplitMeetApi {
    //Rutas para los usuarios
    @POST("users")
    suspend fun register(
        @Body request: User
    ): RegisterResponse

    @POST("users/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponseDto

    @GET("users/search")
    suspend fun searchUsers(
        @Query("username") username: String
    ): List<SearchUserDto>

    //Rutas para las salidas
    @GET("outings/me")
    suspend fun getOutings(): List<OutingDto>

    @GET("outings/{id}")
    suspend fun getOutingById(@Path("id") id: Long): OutingDetailDto

    @POST("outings")
    suspend fun createOuting(
        @Body request: CreateOutingRequest
    ): CreateOutingResponse

    @PATCH("outings/{id}")
    suspend fun updateOuting(
        @Path("id") id: Long,
        @Body request: UpdateOutingRequest
    ): OutingDetailDto

    @DELETE("outings/{id}")
    suspend fun deleteOuting(@Path("id") id: Long)

    //Rutas para participantes
    @GET("outings/{id}/participants")
    suspend fun getParticipants(@Path("id") outingId: Long): List<ParticipantDto>

    @POST("outings/{id}/participants")
    suspend fun addParticipant(
        @Path("id") outingId: Long,
        @Body request: AddParticipantRequest
    ): AddParticipantResponse

    //Rutas para items/productos
    @GET("outings/{id}/items")
    suspend fun getOutingItems(@Path("id") outingId: Long): List<OutingItemDto>

    //Rutas para las categorías
    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>

    //Rutas para productos
    @GET("products/category/{categoryId}")
    suspend fun getProductsByCategory(@Path("categoryId") categoryId: Long): List<ProductDto>

    @POST("products")
    suspend fun createProduct(@Body request: CreateProductRequest): ProductDto

    @GET("outings/{id}/items")
    suspend fun getOutingProducts(@Path("id") outingId: Long): List<OutingProductDto>

    @POST("outings/{id}/items")
    suspend fun addOutingItem(
        @Path("id") outingId: Long,
        @Body request: CreateOutingItemRequest
    ): OutingProductDto

    @DELETE("outings/{outingId}/items/{itemId}")
    suspend fun deleteOutingItem(
        @Path("outingId") outingId: Long,
        @Path("itemId") itemId: Long
    )
}