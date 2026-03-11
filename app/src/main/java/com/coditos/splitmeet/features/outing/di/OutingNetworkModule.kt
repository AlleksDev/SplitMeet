package com.coditos.splitmeet.features.outing.di

import com.coditos.splitmeet.core.di.SplitmeetRetrofit
import com.coditos.splitmeet.features.outing.data.datasources.remote.api.OutingApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OutingNetworkModule {
    @Provides
    @Singleton
    fun provideOutingApi(
        @SplitmeetRetrofit retrofit: Retrofit
    ): OutingApi {
        return retrofit.create(OutingApi::class.java)
    }
}
