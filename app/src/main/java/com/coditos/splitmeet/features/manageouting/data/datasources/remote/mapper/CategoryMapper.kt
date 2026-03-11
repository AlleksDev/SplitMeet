package com.coditos.splitmeet.features.manageOuting.data.datasources.remote.mapper

import com.coditos.splitmeet.features.manageOuting.data.datasources.remote.model.CategoryDto
import com.coditos.splitmeet.features.manageOuting.domain.entities.Category

fun CategoryDto.toDomain(): Category {
    return Category(
        id = this.id,
        name = this.name,
        icon = this.icon
    )
}

fun List<CategoryDto>.toDomainList(): List<Category> {
    return this.map { it.toDomain() }
}
