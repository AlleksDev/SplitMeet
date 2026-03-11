package com.coditos.splitmeet.features.detailOuting.di

import com.coditos.splitmeet.features.detailOuting.data.repositories.DetailOutingRepositoryImpl
import com.coditos.splitmeet.features.detailOuting.domain.repositories.DetailOutingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DetailOutingRepositoryModule {
    @Binds
    abstract fun bindDetailOutingRepository(
        detailOutingRepositoryImpl: DetailOutingRepositoryImpl
    ): DetailOutingRepository
}
