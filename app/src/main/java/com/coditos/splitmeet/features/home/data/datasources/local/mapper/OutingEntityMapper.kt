package com.coditos.splitmeet.features.home.data.datasources.local.mapper

import com.coditos.splitmeet.core.database.entities.OutingEntity
import com.coditos.splitmeet.features.home.domain.entities.Outing


fun OutingEntity.toDomain() = Outing(id,
                                    name,
                                    description,
                                    categoryName,
                                    splitType,
                                    totalAmount,
                                    participantCount,
                                    paidCount)