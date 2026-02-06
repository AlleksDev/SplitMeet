package com.coditos.splitmeet.features.home.domain.entities

data class Outing(
    val Name: String,
    val Description: String,
    val CategoryName: String,
    val SplitType: String,
    val TotalAmount: Float,
    val ParticipantCount: Int,
    val PaidCount: Int
)