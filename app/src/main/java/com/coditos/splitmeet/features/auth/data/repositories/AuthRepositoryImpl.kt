package com.coditos.splitmeet.features.auth.data.repositories

import com.coditos.splitmeet.features.auth.data.datasources.AuthRemoteDataSource
import com.coditos.splitmeet.features.auth.domain.entities.LoginCredentials
import com.coditos.splitmeet.features.auth.domain.entities.RegisterData
import com.coditos.splitmeet.features.auth.domain.entities.User
import com.coditos.splitmeet.features.auth.domain.repositories.AuthRepository

/**
 * Implementation of AuthRepository
 */
class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    
    override suspend fun login(credentials: LoginCredentials): NetworkResult<User> {
        return remoteDataSource.login(credentials.email, credentials.password)
            .map { it.toDomain() }
    }
    
    override suspend fun register(data: RegisterData): NetworkResult<User> {
        return remoteDataSource.register(data.name, data.email, data.password)
            .map { it.toDomain() }
    }
    
    override suspend fun logout(): NetworkResult<Unit> {
        return remoteDataSource.logout()
    }
    
    override suspend fun getCurrentUser(): NetworkResult<User?> {
        return remoteDataSource.getCurrentUser()
            .map { it?.toDomain() }
    }
}
