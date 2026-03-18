package com.coditos.splitmeet.features.detailOuting.data.datasources.local.mapper

import com.coditos.splitmeet.core.database.entities.ParticipantEntity
import com.coditos.splitmeet.features.detailOuting.domain.entities.Participant

/**
 * Mapper for ParticipantEntity (Room) to Participant (Domain)
 */
fun ParticipantEntity.toDomain() = Participant(
    id = this.id,
    outingId = this.outingId,
    userId = this.userId,
    username = this.username,
    name = this.name,
    status = this.status,
    paymentId = this.paymentId,
    paymentStatus = this.paymentStatus,
    amountOwed = this.amountOwed,
    customAmount = this.customAmount
)

fun List<ParticipantEntity>.toDomainList() = this.map { it.toDomain() }

/**
 * Reverse mapper: Participant (Domain) to ParticipantEntity (Room)
 * Used for persisting domain objects to local database
 */
fun Participant.toEntity() = ParticipantEntity(
    id = this.id,
    outingId = this.outingId,
    userId = this.userId,
    username = this.username,
    name = this.name,
    status = this.status,
    paymentId = this.paymentId,
    paymentStatus = this.paymentStatus,
    amountOwed = this.amountOwed,
    customAmount = this.customAmount
)

fun List<Participant>.toEntityList() = this.map { it.toEntity() }
