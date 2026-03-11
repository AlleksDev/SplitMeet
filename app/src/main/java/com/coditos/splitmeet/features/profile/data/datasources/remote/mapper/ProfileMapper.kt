package com.coditos.splitmeet.features.profile.data.datasources.remote.mapper

import com.coditos.splitmeet.features.profile.data.datasources.remote.model.UserProfileDto
import com.coditos.splitmeet.features.profile.domain.entities.UserProfile

fun UserProfileDto.toDomain(): UserProfile = UserProfile(
    id = id,
    username = username,
    name = name,
    email = email,
    phone = phone.orEmpty()
)
