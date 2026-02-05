package com.coditos.splitmeet.features.home.domain.entities

data class Outing(
    val title: String,
    val category: String,
    val total: String,
    val perPerson: String,
    val attendees: Int,
    val paid: Int
)