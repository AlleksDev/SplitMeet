package com.coditos.splitmeet.features.notification.data.datasources.remote.api

import com.coditos.splitmeet.features.notification.data.datasources.remote.model.NotificationPageResponse
import com.coditos.splitmeet.features.notification.data.datasources.remote.model.RespondInvitationRequest
import com.coditos.splitmeet.features.notification.data.datasources.remote.model.RegisterDeviceTokenRequest
import com.coditos.splitmeet.features.notification.data.datasources.remote.model.MessageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApi {

    @GET("notifications")
    suspend fun getNotifications(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): NotificationPageResponse

    @PATCH("groups/{id}/invitation")
    suspend fun respondGroupInvitation(
        @Path("id") groupId: Long,
        @Body request: RespondInvitationRequest
    ): MessageResponse

    @PATCH("outings/{id}/participants/confirm")
    suspend fun respondOutingInvitation(
        @Path("id") outingId: Long,
        @Body request: RespondInvitationRequest
    ): MessageResponse

    @POST("notifications/device-token")
    suspend fun registerDeviceToken(
        @Body request: RegisterDeviceTokenRequest
    ): MessageResponse
}
