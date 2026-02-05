package com.coditos.splitmeet.features.home.data.datasources.remote.mapper

import com.coditos.splitmeet.features.home.data.datasources.remote.model.OutingDto
import com.coditos.splitmeet.features.home.domain.entities.Outing

fun OutingDto.toDomain(): Outing {
    return Outing(
        title = this.title,
        category = this.category,
        total = this.total,
        perPerson = this.perPerson,
        attendees = this.attendees,
        paid = this.paid
    )
}