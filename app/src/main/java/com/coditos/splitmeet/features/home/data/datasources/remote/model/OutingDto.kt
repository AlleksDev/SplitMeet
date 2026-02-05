package com.coditos.splitmeet.features.home.data.datasources.remote.model

data class OutingDto(
    val title: String,
    val category: String,
    val total: String,
    val perPerson: String,
    val attendees: Int,
    val paid: Int
)
