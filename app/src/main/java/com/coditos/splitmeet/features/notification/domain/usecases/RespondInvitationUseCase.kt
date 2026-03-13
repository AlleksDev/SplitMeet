package com.coditos.splitmeet.features.notification.domain.usecases

import com.coditos.splitmeet.features.notification.domain.repositories.NotificationRepository
import javax.inject.Inject

class RespondInvitationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend fun respondGroup(groupId: Long, accept: Boolean): Result<String> =
        repository.respondGroupInvitation(groupId, accept)

    suspend fun respondOuting(outingId: Long, accept: Boolean): Result<String> =
        repository.respondOutingInvitation(outingId, accept)
}
