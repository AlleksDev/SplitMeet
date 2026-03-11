package com.coditos.splitmeet.features.group.data.repositories

import android.util.Log
import com.coditos.splitmeet.features.group.data.datasources.remote.api.GroupApi
import com.coditos.splitmeet.features.group.data.datasources.remote.mapper.toDomain
import com.coditos.splitmeet.features.group.data.datasources.remote.model.CreateGroupRequest
import com.coditos.splitmeet.features.group.data.datasources.remote.model.InviteMemberRequest
import com.coditos.splitmeet.features.group.domain.entities.Group
import com.coditos.splitmeet.features.group.domain.entities.GroupMember
import com.coditos.splitmeet.features.group.domain.repositories.GroupRepository
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepositoryImpl @Inject constructor(
    private val groupApi: GroupApi
) : GroupRepository {

    companion object {
        private const val TAG = "GroupRepo"
    }

    override suspend fun getMyGroups(): Result<List<Group>> {
        return try {
            val response = groupApi.getMyGroups()
            val groups = response.data?.map { it.toDomain() } ?: emptyList()
            Result.success(groups)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching groups", e)
            Result.failure(e)
        }
    }

    override suspend fun getGroupById(groupId: Long): Result<Group> {
        return try {
            val dto = groupApi.getGroupById(groupId)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching group detail", e)
            Result.failure(e)
        }
    }

    override suspend fun createGroup(name: String, description: String): Result<Group> {
        return try {
            val dto = groupApi.createGroup(CreateGroupRequest(name, description))
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Log.e(TAG, "Error creating group", e)
            Result.failure(e)
        }
    }

    override suspend fun updateGroup(groupId: Long, name: String, description: String): Result<Group> {
        return try {
            val dto = groupApi.updateGroup(groupId, CreateGroupRequest(name, description))
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Log.e(TAG, "Error updating group", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteGroup(groupId: Long): Result<Unit> {
        return try {
            groupApi.deleteGroup(groupId)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting group", e)
            Result.failure(e)
        }
    }

    override suspend fun getMembers(groupId: Long): Result<List<GroupMember>> {
        return try {
            val members = groupApi.getMembers(groupId).map { it.toDomain() }
            Result.success(members)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching members", e)
            Result.failure(e)
        }
    }

    override suspend fun inviteMember(groupId: Long, username: String): Result<Unit> {
        return try {
            groupApi.inviteMember(groupId, InviteMemberRequest(username))
            Result.success(Unit)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val message = try {
                JSONObject(errorBody ?: "").getString("error")
            } catch (_: Exception) {
                "Error al invitar miembro"
            }
            Log.e(TAG, "Error inviting member: $message", e)
            Result.failure(Exception(message))
        } catch (e: Exception) {
            Log.e(TAG, "Error inviting member", e)
            Result.failure(e)
        }
    }

    override suspend fun removeMember(groupId: Long, userId: Long): Result<Unit> {
        return try {
            groupApi.removeMember(groupId, userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error removing member", e)
            Result.failure(e)
        }
    }
}
