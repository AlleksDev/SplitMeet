package com.coditos.splitmeet.features.manageOuting.di

import com.coditos.splitmeet.core.di.SplitmeetRetrofit
import com.coditos.splitmeet.features.manageOuting.data.datasources.remote.api.ManageOutingApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManageOutingNetworkModule {
    @Provides
    @Singleton
    fun provideManageOutingApi(
        @SplitmeetRetrofit retrofit: Retrofit
    ): ManageOutingApi {
        return retrofit.create(ManageOutingApi::class.java)
    }
}
