package com.coditos.splitmeet.features.home.data.repositories

import android.util.Log
import com.coditos.splitmeet.core.database.dao.OutingDao
import com.coditos.splitmeet.features.home.data.datasources.local.mapper.toDomain
import com.coditos.splitmeet.features.home.data.datasources.remote.api.OutingApi
import com.coditos.splitmeet.features.home.data.datasources.remote.mapper.toDomain
import com.coditos.splitmeet.features.home.data.datasources.remote.mapper.toEntity
import com.coditos.splitmeet.features.home.domain.entities.Outing
import com.coditos.splitmeet.features.home.domain.repositories.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: OutingApi,
    private val dao: OutingDao
) : HomeRepository {

    override fun getOutings(): Flow<List<Outing>> {
        return dao.getAllOutings().map {
            entities -> entities.map { it.toDomain() }
        }
    }

    override suspend fun syncOutings() {
        withContext(Dispatchers.IO) {
            try {
                val remoteOutings = api.getOutings()
                dao.insertOutings(remoteOutings.map { it.toEntity() })
            } catch (e: UnknownHostException) {
                Log.w("HomeRepo", "Sin internet, usando caché local")  // No relanzar — es comportamiento esperado
            } catch (e: Exception) {
                Log.e("HomeRepo", "Error inesperado: ${e.message}", e)
                throw e  // Solo relanza errores inesperados
            }
        }
    }
}
