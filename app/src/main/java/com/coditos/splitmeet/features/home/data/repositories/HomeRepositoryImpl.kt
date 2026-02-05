package com.coditos.splitmeet.features.home.data.repositories

import com.coditos.splitmeet.core.network.SplitMeetApi
import com.coditos.splitmeet.features.home.data.datasources.remote.mapper.toDomain
import com.coditos.splitmeet.features.home.domain.entities.Outing
import com.coditos.splitmeet.features.home.domain.repositories.HomeRepository

class HomeRepositoryImpl(
    private val api: SplitMeetApi
) : HomeRepository {

    override suspend fun getOutings(): List<Outing> {
        val response = api.getOutings()
        return response.map { it.toDomain() }
    }

    override suspend fun getOutingById(id: String): Outing {
        TODO("Not yet implemented")
    }
}
