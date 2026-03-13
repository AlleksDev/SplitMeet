package com.coditos.splitmeet.features.group.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class GroupDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("owner_id") val ownerId: Long,
    @SerializedName("member_count") val memberCount: Int?,
    @SerializedName("owner_username") val ownerUsername: String?,
    @SerializedName("is_active") val isActive: Boolean?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)

data class GroupMemberDto(
    @SerializedName("id") val id: Long,
    @SerializedName("group_id") val groupId: Long,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("role") val role: String,
    @SerializedName("status") val status: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?
)

data class CreateGroupRequest(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String
)

data class InviteMemberRequest(
    @SerializedName("username") val username: String
)

data class PaginatedGroupsResponse(
    @SerializedName("data") val data: List<GroupDto>?,
    @SerializedName("total") val total: Int?,
    @SerializedName("page") val page: Int?,
    @SerializedName("limit") val limit: Int?
)
