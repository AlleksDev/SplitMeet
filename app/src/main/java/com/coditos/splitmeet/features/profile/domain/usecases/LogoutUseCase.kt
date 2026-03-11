package com.coditos.splitmeet.features.profile.domain.usecases

import com.coditos.splitmeet.features.profile.domain.repositories.ProfileRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke() = repository.logout()
}
