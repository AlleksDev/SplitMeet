package com.coditos.splitmeet.core.di

import com.coditos.splitmeet.core.hardware.data.AndroidAppVibrator
import com.coditos.splitmeet.core.hardware.data.AndroidFingerPrintManager
import com.coditos.splitmeet.core.hardware.data.AndroidHapticFeedbackManager
import com.coditos.splitmeet.core.hardware.data.AndroidQrScanner
import com.coditos.splitmeet.core.hardware.domain.AppVibrator
import com.coditos.splitmeet.core.hardware.domain.FingerPrintManager
import com.coditos.splitmeet.core.hardware.domain.HapticFeedbackManager
import com.coditos.splitmeet.core.hardware.domain.QrScanner
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareModule {

    @Binds
    @Singleton
    abstract fun bindFingerPrintManager(
        impl: AndroidFingerPrintManager
    ): FingerPrintManager

    @Binds
    @Singleton
    abstract fun bindAppVibrator(
        impl: AndroidAppVibrator
    ): AppVibrator

    @Binds
    @Singleton
    abstract fun bindQrScanner(
        impl: AndroidQrScanner
    ): QrScanner

    @Binds
    @Singleton
    abstract fun bindHapticFeedbackManager(
        impl: AndroidHapticFeedbackManager
    ): HapticFeedbackManager
}