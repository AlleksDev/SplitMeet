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
data class OutingDetail(val outingId: Long)