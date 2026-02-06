package com.coditos.splitmeet.core.network

import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.LoginRequest
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.LoginResponseDto
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.User
import com.coditos.splitmeet.features.auth.domain.entities.RegisterResponse
import com.coditos.splitmeet.features.home.data.datasources.remote.model.OutingDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    //Rutas para las salidas
    @GET("outings/me")
    suspend fun getOutings(): List<OutingDto>

    @GET("outings/{id}")
    suspend fun getOutingById(@Path("id") id: String): OutingDto
}