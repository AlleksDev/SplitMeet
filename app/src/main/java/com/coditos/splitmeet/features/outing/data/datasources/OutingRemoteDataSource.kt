package com.coditos.splitmeet.features.outing.data.datasources

import com.coditos.splitmeet.core.network.safeApiCall
import com.coditos.splitmeet.features.outing.data.datasources.dto.CreateOutingRequestDto
import com.coditos.splitmeet.features.outing.data.datasources.dto.OutingDto

/**
 * Remote data source for Outing operations
 */
class OutingRemoteDataSource(
    private val apiService: OutingApiService
) {
    
    suspend fun getOutings(): NetworkResult<List<OutingDto>> {
        return safeApiCall { apiService.getOutings() }
    }
    
    suspend fun getActiveOutings(): NetworkResult<List<OutingDto>> {
        return safeApiCall { apiService.getActiveOutings() }
    }
    
    suspend fun getOutingHistory(): NetworkResult<List<OutingDto>> {
        return safeApiCall { apiService.getOutingHistory() }
    }
    
    suspend fun getOutingById(id: String): NetworkResult<OutingDto> {
        return safeApiCall { apiService.getOutingById(id) }
    }
    
    suspend fun createOuting(
        title: String,
        category: String,
        totalAmount: Double,
        participantIds: List<String>
    ): NetworkResult<OutingDto> {
        return safeApiCall {
            apiService.createOuting(
                CreateOutingRequestDto(title, category, totalAmount, participantIds)
            )
        }
    }
    
    suspend fun closeOuting(id: String): NetworkResult<OutingDto> {
        return safeApiCall { apiService.closeOuting(id) }
    }
    
    suspend fun deleteOuting(id: String): NetworkResult<Unit> {
        return safeApiCall { apiService.deleteOuting(id) }
    }
}
