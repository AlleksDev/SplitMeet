package com.coditos.splitmeet.core.di

import android.content.Context
import com.coditos.splitmeet.BuildConfig
import com.coditos.splitmeet.core.network.SplitMeetApi
import com.coditos.splitmeet.core.network.interceptor.AuthInterceptor
import com.coditos.splitmeet.core.network.interceptor.provideLoggingInterceptor
import com.coditos.splitmeet.core.storage.TokenDataStore
import com.coditos.splitmeet.features.auth.data.repositories.AuthRepositoryImpl
import com.coditos.splitmeet.features.auth.domain.repositories.AuthRepository
import com.coditos.splitmeet.features.home.data.repositories.HomeRepositoryImpl
import com.coditos.splitmeet.features.home.domain.repositories.HomeRepository
import com.coditos.splitmeet.features.outing.data.repositories.OutingRepositoryImpl
import com.coditos.splitmeet.features.outing.domain.repositories.OutingRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppContainer(context: Context) {

    val tokenDataStore: TokenDataStore by lazy {
        TokenDataStore(context)
    }

    private val tokenProvider: () -> String = {
        runBlocking {
            tokenDataStore.getToken() ?: ""
        }
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(tokenProvider))
            .addInterceptor(provideLoggingInterceptor())
            .build()
    }

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofit = createRetrofit(BuildConfig.BASE_URL)

    val splitMeetApi: SplitMeetApi by lazy {
        retrofit.create(SplitMeetApi::class.java)
    }

    val homeRepository: HomeRepository by lazy {
        HomeRepositoryImpl(splitMeetApi)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(splitMeetApi)
    }

    val outingRepository: OutingRepository by lazy {
        OutingRepositoryImpl(splitMeetApi)
    }
}
