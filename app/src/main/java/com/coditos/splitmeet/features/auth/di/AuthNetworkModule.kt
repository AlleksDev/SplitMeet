package com.coditos.splitmeet.features.auth.di

import com.coditos.splitmeet.core.di.SplitmeetRetrofit
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.api.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthNetworkModule {
    @Provides
    @Singleton
    fun provideAuthApi(
        @SplitmeetRetrofit retrofit: Retrofit
    ): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}
