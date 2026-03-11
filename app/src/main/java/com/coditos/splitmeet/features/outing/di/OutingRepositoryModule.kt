package com.coditos.splitmeet.features.outing.di

import com.coditos.splitmeet.features.outing.data.repositories.OutingRepositoryImpl
import com.coditos.splitmeet.features.outing.domain.repositories.OutingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class OutingRepositoryModule {
    @Binds
    abstract fun bindOutingRepository(
        outingRepositoryImpl: OutingRepositoryImpl
    ): OutingRepository
}
