    package com.coditos.splitmeet.core.di

import android.content.Context
import com.coditos.splitmeet.core.network.SplitMeetApi
import com.coditos.splitmeet.features.home.domain.repositories.HomeRepository
import com.coditos.splitmeet.features.home.data.repositories.HomeRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
    class AppContainer(context: Context) {
    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val SplitMeetApiRetrofit = createRetrofit("https://frimeet.fun/")


    val splitMeetApi: SplitMeetApi by lazy {
        SplitMeetApiRetrofit.create(SplitMeetApi::class.java)
    }

    val homeRepository: HomeRepository by lazy {
        HomeRepositoryImpl(splitMeetApi)
    }
}
