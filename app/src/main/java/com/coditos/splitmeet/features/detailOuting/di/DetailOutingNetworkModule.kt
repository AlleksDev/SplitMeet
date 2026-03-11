package com.coditos.splitmeet.features.detailOuting.di

import com.coditos.splitmeet.core.di.SplitmeetRetrofit
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.api.DetailOutingApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DetailOutingNetworkModule {
    @Provides
    @Singleton
    fun provideDetailOutingApi(
        @SplitmeetRetrofit retrofit: Retrofit
    ): DetailOutingApi {
        return retrofit.create(DetailOutingApi::class.java)
    }
}
