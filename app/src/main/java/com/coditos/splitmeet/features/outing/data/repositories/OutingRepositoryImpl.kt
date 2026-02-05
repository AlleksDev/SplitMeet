package com.coditos.splitmeet.features.outing.data.repositories

import com.coditos.splitmeet.features.outing.data.datasources.OutingRemoteDataSource
import com.coditos.splitmeet.features.outing.data.datasources.dto.toDomainList
import com.coditos.splitmeet.features.outing.domain.entities.CreateOutingData
import com.coditos.splitmeet.features.outing.domain.entities.Outing
import com.coditos.splitmeet.features.outing.domain.repositories.HomeRepository

/**
 * Implementation of OutingRepository
 */
class OutingRepositoryImpl(
    private val remoteDataSource: OutingRemoteDataSource
) : HomeRepository {
    
    override suspend fun getOutings(): NetworkResult<List<Outing>> {
        return remoteDataSource.getOutings().map { it.toDomainList() }
    }
    
    override suspend fun getActiveOutings(): NetworkResult<List<Outing>> {
        return remoteDataSource.getActiveOutings().map { it.toDomainList() }
    }
    
    override suspend fun getOutingHistory(): NetworkResult<List<Outing>> {
        return remoteDataSource.getOutingHistory().map { it.toDomainList() }
    }
    
    override suspend fun getOutingById(id: String): NetworkResult<Outing> {
        return remoteDataSource.getOutingById(id).map { it.toDomain() }
    }
    
    override suspend fun createOuting(data: CreateOutingData): NetworkResult<Outing> {
        return remoteDataSource.createOuting(
            data.title,
            data.category,
            data.totalAmount,
            data.participantIds
        ).map { it.toDomain() }
    }
    
    override suspend fun closeOuting(id: String): NetworkResult<Outing> {
        return remoteDataSource.closeOuting(id).map { it.toDomain() }
    }
    
    override suspend fun deleteOuting(id: String): NetworkResult<Unit> {
        return remoteDataSource.deleteOuting(id)
    }
}
