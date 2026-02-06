package com.coditos.splitmeet.features.outing.data.datasources.remote.mapper

import com.coditos.splitmeet.features.outing.data.datasources.remote.model.CreateOutingResponse
import com.coditos.splitmeet.features.outing.domain.entities.CreatedOuting

fun CreateOutingResponse.toDomain(): CreatedOuting {
    return CreatedOuting(
        id = this.id ?: 0,
        name = this.name ?: "",
        description = this.description,
        categoryId = this.categoryId,
        creatorId = this.creatorId ?: 0,
        outingDate = this.outingDate ?: "",
        splitType = this.splitType ?: "equal",
        totalAmount = this.totalAmount ?: 0.0,
        status = this.status ?: "active",
        isEditable = this.isEditable ?: true
    )
}
