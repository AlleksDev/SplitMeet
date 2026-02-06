package com.coditos.splitmeet.features.home.data.datasources.remote.mapper

import com.coditos.splitmeet.features.home.data.datasources.remote.model.OutingDto
import com.coditos.splitmeet.features.home.domain.entities.Outing

fun OutingDto.toDomain(): Outing {
    return Outing(
        Name = this.Name,
        Description = this.Description,
        CategoryName = this.CategoryName,
        SplitType = this.SplitType,
        TotalAmount = this.TotalAmount,
        ParticipantCount = this.ParticipantCount,
        PaidCount = this.PaidCount
    )
}