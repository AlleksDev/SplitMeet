package com.coditos.splitmeet.features.manageOuting.di

import com.coditos.splitmeet.features.manageOuting.data.repositories.ManageOutingRepositoryImpl
import com.coditos.splitmeet.features.manageOuting.domain.repositories.ManageOutingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ManageOutingRepositoryModule {
    @Binds
    abstract fun bindManageOutingRepository(
        manageOutingRepositoryImpl: ManageOutingRepositoryImpl
    ): ManageOutingRepository
}
