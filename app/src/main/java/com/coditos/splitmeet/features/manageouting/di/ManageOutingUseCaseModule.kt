package com.coditos.splitmeet.features.manageOuting.di

import com.coditos.splitmeet.features.manageOuting.domain.repositories.ManageOutingRepository
import com.coditos.splitmeet.features.manageOuting.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManageOutingUseCaseModule {

    @Provides
    @Singleton
    fun provideCreateOutingUseCase(repository: ManageOutingRepository): CreateOutingUseCase {
        return CreateOutingUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetCategoriesUseCase(repository: ManageOutingRepository): GetCategoriesUseCase {
        return GetCategoriesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideManageOutingUseCases(
        createOuting: CreateOutingUseCase,
        getCategories: GetCategoriesUseCase
    ): ManageOutingUseCases {
        return ManageOutingUseCases(
            createOuting = createOuting,
            getCategories = getCategories
        )
    }
}