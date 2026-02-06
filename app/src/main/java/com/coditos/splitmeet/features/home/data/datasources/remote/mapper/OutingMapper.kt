package com.coditos.splitmeet.features.home.data.datasources.remote.mapper

import com.coditos.splitmeet.features.home.data.datasources.remote.model.OutingDto
import com.coditos.splitmeet.features.home.domain.entities.Outing

fun OutingDto.toDomain(): Outing {
    return Outing(
        id = this.id,
        name = this.name,
        description = this.description ?: "",
        categoryName = this.categoryName,
        splitType = this.splitType,
        totalAmount = this.totalAmount,
        participantCount = this.participantCount,
        paidCount = this.paidCount
    )
}
