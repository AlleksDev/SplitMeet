package com.coditos.splitmeet.features.detailOuting.domain.entities

data class SearchUser(
    val id: Long,
    val username: String,
    val name: String,
    val email: String?
) {
    val displayInitial: Char
        get() = name.firstOrNull()?.uppercaseChar() ?: username.firstOrNull()?.uppercaseChar() ?: '?'
}
