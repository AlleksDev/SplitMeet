package com.coditos.splitmeet.features.outing.di

import com.coditos.splitmeet.features.outing.domain.repositories.OutingRepository
import com.coditos.splitmeet.features.outing.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OutingUseCaseModule {

    @Provides
    @Singleton
    fun provideCreateOutingUseCase(repository: OutingRepository): CreateOutingUseCase {
        return CreateOutingUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetCategoriesUseCase(repository: OutingRepository): GetCategoriesUseCase {
        return GetCategoriesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideOutingUseCases(
        createOuting: CreateOutingUseCase,
        getCategories: GetCategoriesUseCase
    ): OutingUseCases {
        return OutingUseCases(
            createOuting = createOuting,
            getCategories = getCategories
        )
    }
}