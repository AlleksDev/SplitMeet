package com.coditos.splitmeet.features.detailOuting.data.datasources.remote.mapper

import com.coditos.splitmeet.core.database.entities.ParticipantEntity
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.ParticipantDto
import com.coditos.splitmeet.features.detailOuting.domain.entities.Participant

fun ParticipantDto.toDomain(): Participant {
    return Participant(
        id = this.id ?: 0,
        outingId = this.outingId ?: 0,
        userId = this.userId ?: 0,
        username = this.username ?: "",
        name = this.name ?: this.username ?: "",
        status = this.status ?: "pending",
        paymentId = this.paymentId,
        paymentStatus = this.paymentStatus,
        amountOwed = this.amountOwed ?: 0.0,
        customAmount = this.customAmount
    )
}

fun ParticipantDto.toEntity(): ParticipantEntity {
    return ParticipantEntity(
        id = this.id ?: 0,
        outingId = this.outingId ?: 0,
        userId = this.userId ?: 0,
        username = this.username ?: "",
        name = this.name ?: this.username ?: "",
        status = this.status ?: "pending",
        paymentId = this.paymentId,
        paymentStatus = this.paymentStatus,
        amountOwed = this.amountOwed ?: 0.0,
        customAmount = this.customAmount,
        joinedAt = this.joinedAt
    )
}

fun List<ParticipantDto>.toDomainList(): List<Participant> {
    return this.map { it.toDomain() }
}

fun List<ParticipantDto>.toEntityList(): List<ParticipantEntity> {
    return this.map { it.toEntity() }
}
