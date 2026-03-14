package com.coditos.splitmeet.features.notification.domain.repositories

import com.coditos.splitmeet.features.notification.domain.entities.Notification
import kotlinx.coroutines.flow.SharedFlow

interface NotificationRepository {

    val notifications: SharedFlow<Notification>

    fun startListening()

    fun stopListening()

    suspend fun getNotifications(page: Int, limit: Int): Result<List<Notification>>

    suspend fun respondGroupInvitation(groupId: Long, accept: Boolean): Result<String>

    suspend fun respondOutingInvitation(outingId: Long, accept: Boolean): Result<String>

    suspend fun registerDeviceToken(token: String): Result<String>
}

