package com.coditos.splitmeet.features.profile.domain.repositories

import com.coditos.splitmeet.features.profile.domain.entities.UserProfile

interface ProfileRepository {
    suspend fun getProfile(): Result<UserProfile>
    suspend fun logout()
}
