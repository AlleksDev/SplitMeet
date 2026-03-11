package com.coditos.splitmeet.features.product.data.datasources.remote.api

import com.coditos.splitmeet.features.product.data.datasources.remote.model.CreateOutingItemRequest
import com.coditos.splitmeet.features.product.data.datasources.remote.model.CreateProductRequest
import com.coditos.splitmeet.features.product.data.datasources.remote.model.OutingProductDto
import com.coditos.splitmeet.features.product.data.datasources.remote.model.ProductDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductApi {

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
