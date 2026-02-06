package com.coditos.splitmeet.features.home.data.datasources.remote.model

data class OutingDto(
    val Name: String,
    val Description: String,
    val CategoryName: String,
    val SplitType: String,
    val TotalAmount: Float,
    val ParticipantCount: Int,
    val PaidCount: Int
)
