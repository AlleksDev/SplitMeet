package com.coditos.splitmeet.features.profile.data.repositories

import android.util.Log
import com.coditos.splitmeet.core.storage.TokenDataStore
import com.coditos.splitmeet.features.profile.data.datasources.remote.api.ProfileApi
import com.coditos.splitmeet.features.profile.data.datasources.remote.mapper.toDomain
import com.coditos.splitmeet.features.profile.data.datasources.remote.model.UpdateProfileRequest
import com.coditos.splitmeet.features.profile.domain.entities.UserProfile
import com.coditos.splitmeet.features.profile.domain.repositories.ProfileRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val profileApi: ProfileApi,
    private val tokenDataStore: TokenDataStore
) : ProfileRepository {

    companion object {
        private const val TAG = "ProfileRepo"
    }

    override suspend fun getProfile(): Result<UserProfile> {
        return try {
            val dto = profileApi.getProfile()
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching profile", e)
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(name: String, phone: String, password: String): Result<UserProfile> {
        return try {
            val response = profileApi.updateProfile(
                UpdateProfileRequest(
                    name = name,
                    phone = phone,
                    password = password
                )
            )
            Result.success(response.user.toDomain())
        } catch (e: Exception) {
            Log.e(TAG, "Error updating profile", e)
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        tokenDataStore.clearToken()
    }
}
