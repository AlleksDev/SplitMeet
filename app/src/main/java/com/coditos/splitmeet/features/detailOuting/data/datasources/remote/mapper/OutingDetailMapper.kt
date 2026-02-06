package com.coditos.splitmeet.features.detailOuting.data.datasources.remote.mapper

import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.OutingDetailDto
import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingDetail

fun OutingDetailDto.toDomain(): OutingDetail {
    return OutingDetail(
        id = this.id ?: 0,
        name = this.name ?: "",
        description = this.description,
        categoryId = this.categoryId,
        categoryName = this.categoryName,
        groupId = this.groupId,
        creatorId = this.creatorId ?: 0,
        outingDate = this.outingDate ?: "",
        splitType = this.splitType ?: "equal",
        totalAmount = this.totalAmount ?: 0.0,
        status = this.status ?: "active",
        isEditable = this.isEditable ?: true,
        participantCount = this.participantCount ?: 0,
        paidCount = this.paidCount ?: 0
    )
}
