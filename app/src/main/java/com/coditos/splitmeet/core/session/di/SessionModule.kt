package com.coditos.splitmeet.core.session.di

import com.coditos.splitmeet.core.di.SplitmeetRetrofit
import com.coditos.splitmeet.core.session.data.datasources.remote.api.SessionApi
import com.coditos.splitmeet.core.session.data.repositories.SessionRepositoryImpl
import com.coditos.splitmeet.core.session.domain.repositories.SessionRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SessionRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSessionRepository(
        sessionRepositoryImpl: SessionRepositoryImpl
    ): SessionRepository
}

@Module
@InstallIn(SingletonComponent::class)
object SessionNetworkModule {
    @Provides
    @Singleton
    fun provideSessionApi(
        @SplitmeetRetrofit retrofit: Retrofit
    ): SessionApi = retrofit.create(SessionApi::class.java)
}
