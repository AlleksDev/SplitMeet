    package com.coditos.splitmeet.core.di

import android.content.Context
import com.coditos.splitmeet.core.network.SplitMeetApi
import com.coditos.splitmeet.features.outing.data.repositories.OutingRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
    class AppContainer(context: Context) {
    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val SplitMeetApiRetrofit = createRetrofit("https://api.example.com/")


    val splitMeetApi: SplitMeetApi by lazy {
        SplitMeetApiRetrofit.create(SplitMeetApi::class.java)
    }

    val outingsRepository: com.coditos.splitmeet.features.outing.domain.repositories.HomeRepository by lazy {
        OutingRepositoryImpl(splitMeetApi)

    }
}
