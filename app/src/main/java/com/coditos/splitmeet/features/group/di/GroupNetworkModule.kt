package com.coditos.splitmeet.features.group.di

import com.coditos.splitmeet.core.di.SplitmeetRetrofit
import com.coditos.splitmeet.features.group.data.datasources.remote.api.GroupApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GroupNetworkModule {

    @Provides
    @Singleton
    fun provideGroupApi(
        @SplitmeetRetrofit retrofit: Retrofit
    ): GroupApi {
        return retrofit.create(GroupApi::class.java)
    }
}
