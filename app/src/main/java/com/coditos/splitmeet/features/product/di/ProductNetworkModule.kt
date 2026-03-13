package com.coditos.splitmeet.features.product.di

import com.coditos.splitmeet.core.di.SplitmeetRetrofit
import com.coditos.splitmeet.features.product.data.datasources.remote.api.ProductApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductNetworkModule {
    @Provides
    @Singleton
    fun provideProductApi(
        @SplitmeetRetrofit retrofit: Retrofit
    ): ProductApi {
        return retrofit.create(ProductApi::class.java)
    }
}
