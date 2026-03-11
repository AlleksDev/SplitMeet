package com.coditos.splitmeet.features.group.di

import com.coditos.splitmeet.features.group.data.repositories.GroupRepositoryImpl
import com.coditos.splitmeet.features.group.domain.repositories.GroupRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GroupRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGroupRepository(impl: GroupRepositoryImpl): GroupRepository
}
