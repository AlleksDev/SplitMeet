package com.coditos.splitmeet.features.home.domain.repositories

import com.coditos.splitmeet.features.home.domain.entities.Outing
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getOutings(): Flow<List<Outing>>
    suspend fun syncOutings()
}
