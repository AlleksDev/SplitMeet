package com.coditos.splitmeet.features.detailOuting.data.datasources.remote.mapper

import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.SearchUserDto
import com.coditos.splitmeet.features.detailOuting.domain.entities.SearchUser

fun SearchUserDto.toDomain(): SearchUser {
    return SearchUser(
        id = this.id ?: 0,
        username = this.username ?: "",
        name = this.name ?: this.username ?: "",
        email = this.email
    )
}

fun List<SearchUserDto>.toDomainList(): List<SearchUser> {
    return this.map { it.toDomain() }
}
