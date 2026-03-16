package com.coditos.splitmeet.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Login

@Serializable
object Register

@Serializable
object CreateOuting

@Serializable
data class OutingDetail(val outingId: Long, val joinAutomatically: Boolean = false)

@Serializable
data class AddProducts(
    val outingId: Long,
    val categoryId: Long,
    val categoryName: String
)

@Serializable
object Notifications

@Serializable
object Profile

@Serializable
object CreateGroup

@Serializable
data class GroupDetail(val groupId: Long)

@Serializable
data class ShowOutingQr(val outingId: Long)