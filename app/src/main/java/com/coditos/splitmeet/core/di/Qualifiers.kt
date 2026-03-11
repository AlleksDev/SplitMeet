package com.coditos.splitmeet.core.di

import jakarta.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SplitmeetRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SseClient