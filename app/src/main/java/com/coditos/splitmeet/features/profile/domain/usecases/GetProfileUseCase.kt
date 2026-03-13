package com.coditos.splitmeet.features.profile.domain.usecases

import com.coditos.splitmeet.features.profile.domain.entities.UserProfile
import com.coditos.splitmeet.features.profile.domain.repositories.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<UserProfile> = repository.getProfile()
}
