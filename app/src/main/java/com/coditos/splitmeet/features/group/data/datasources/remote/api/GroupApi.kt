package com.coditos.splitmeet.features.group.data.datasources.remote.api

import com.coditos.splitmeet.features.group.data.datasources.remote.model.CreateGroupRequest
import com.coditos.splitmeet.features.group.data.datasources.remote.model.GroupDto
import com.coditos.splitmeet.features.group.data.datasources.remote.model.GroupMemberDto
import com.coditos.splitmeet.features.group.data.datasources.remote.model.InviteMemberRequest
import com.coditos.splitmeet.features.group.data.datasources.remote.model.PaginatedGroupsResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupApi {

    @GET("groups")
    suspend fun getMyGroups(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): PaginatedGroupsResponse

    @GET("groups/{id}")
    suspend fun getGroupById(@Path("id") groupId: Long): GroupDto

    @POST("groups")
    suspend fun createGroup(@Body request: CreateGroupRequest): GroupDto

    @PATCH("groups/{id}")
    suspend fun updateGroup(
        @Path("id") groupId: Long,
        @Body request: CreateGroupRequest
    ): GroupDto

    @DELETE("groups/{id}")
    suspend fun deleteGroup(@Path("id") groupId: Long)

    @GET("groups/{id}/members")
    suspend fun getMembers(@Path("id") groupId: Long): List<GroupMemberDto>

    @POST("groups/{id}/invite")
    suspend fun inviteMember(
        @Path("id") groupId: Long,
        @Body request: InviteMemberRequest
    )

    @DELETE("groups/{id}/members/{userId}")
    suspend fun removeMember(
        @Path("id") groupId: Long,
        @Path("userId") userId: Long
    )
}
