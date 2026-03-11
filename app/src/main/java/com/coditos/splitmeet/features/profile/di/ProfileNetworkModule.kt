package com.coditos.splitmeet.features.profile.di

import com.coditos.splitmeet.core.di.SplitmeetRetrofit
import com.coditos.splitmeet.features.profile.data.datasources.remote.api.ProfileApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileNetworkModule {

    @Provides
    @Singleton
    fun provideProfileApi(
        @SplitmeetRetrofit retrofit: Retrofit
    ): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }
}
