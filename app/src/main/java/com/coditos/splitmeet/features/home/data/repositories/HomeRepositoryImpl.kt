package com.coditos.splitmeet.features.home.data.repositories

import com.coditos.splitmeet.features.home.data.datasources.remote.api.OutingApi
import com.coditos.splitmeet.features.home.data.datasources.remote.mapper.toDomain
import com.coditos.splitmeet.features.home.domain.entities.Outing
import com.coditos.splitmeet.features.home.domain.repositories.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: OutingApi
) : HomeRepository {

    override suspend fun getOutings(): List<Outing> {
        val response = api.getOutings()
        return response.map { it.toDomain() }
    }
}
