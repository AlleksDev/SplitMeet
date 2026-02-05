package com.coditos.splitmeet.features.outing.domain.repositories

import com.coditos.splitmeet.features.outing.domain.entities.CreateOutingData
import com.coditos.splitmeet.features.outing.domain.entities.Outing

/**
 * Repository interface for Outing operations
 */
interface HomeRepository {
    suspend fun getOutings():List<Outing>
    suspend fun getActiveOutings(): List<Outing>
    suspend fun getOutingHistory(): List<Outing>
    suspend fun getOutingById(id: String): Outing
    suspend fun createOuting(data: CreateOutingData): Outing
    suspend fun closeOuting(id: String): Outing
    suspend fun deleteOuting(id: String): Unit
}
