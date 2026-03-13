package com.coditos.splitmeet.features.home.di

import com.coditos.splitmeet.features.home.domain.usecases.GetOutingsUseCase
import com.coditos.splitmeet.features.home.domain.usecases.HomeUseCases
import com.coditos.splitmeet.features.home.domain.usecases.SyncOutingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object HomeUseCaseModule {
    @Provides
    fun provideHomeUseCases(
        getOutings: GetOutingsUseCase,
        syncOutings: SyncOutingsUseCase
    ): HomeUseCases = HomeUseCases(
        getOutings = getOutings,
        syncOutings = syncOutings
    )
}