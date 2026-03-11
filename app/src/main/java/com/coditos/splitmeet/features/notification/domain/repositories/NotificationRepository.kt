package com.coditos.splitmeet.features.notification.domain.repositories

import com.coditos.splitmeet.features.notification.domain.entities.Notification
import kotlinx.coroutines.flow.SharedFlow

interface NotificationRepository {

    /** Real-time notifications parsed from the global SSE stream. */
    val notifications: SharedFlow<Notification>

    /** Starts listening to SSE events relevant to this feature. */
    fun startListening()

    /** Stops listening and cleans up. */
    fun stopListening()

    /** Fetches paginated notifications from the REST API. */
    suspend fun getNotifications(page: Int, limit: Int): Result<List<Notification>>

    /** Accept or reject a group invitation. */
    suspend fun respondGroupInvitation(groupId: Long, accept: Boolean): Result<String>

    /** Accept or reject an outing invitation. */
    suspend fun respondOutingInvitation(outingId: Long, accept: Boolean): Result<String>
}

