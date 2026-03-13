package com.coditos.splitmeet.core.session.data.datasources.remote.api

import retrofit2.Response
import retrofit2.http.GET

interface SessionApi {
    @GET("users/profile")
    suspend fun validateSession(): Response<Unit>
}
