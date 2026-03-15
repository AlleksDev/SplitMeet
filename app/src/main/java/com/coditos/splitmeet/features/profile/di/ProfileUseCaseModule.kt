package com.coditos.splitmeet.features.profile.di

import com.coditos.splitmeet.features.profile.domain.repositories.ProfileRepository
import com.coditos.splitmeet.features.profile.domain.usecases.GetProfileUseCase
import com.coditos.splitmeet.features.profile.domain.usecases.LogoutUseCase
import com.coditos.splitmeet.features.profile.domain.usecases.ProfileUseCases
import com.coditos.splitmeet.features.profile.domain.usecases.UpdateProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileUseCaseModule {

    @Provides
    @Singleton
    fun provideGetProfileUseCase(repository: ProfileRepository): GetProfileUseCase {
        return GetProfileUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(repository: ProfileRepository): LogoutUseCase {
        return LogoutUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateProfileUseCase(repository: ProfileRepository): UpdateProfileUseCase {
        return UpdateProfileUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideProfileUseCases(
        getProfileUseCase: GetProfileUseCase,
        logoutUseCase: LogoutUseCase,
        updateProfileUseCase: UpdateProfileUseCase
    ): ProfileUseCases {
        return ProfileUseCases(
            getProfile = getProfileUseCase,
            logout = logoutUseCase,
            updateProfile = updateProfileUseCase
        )
    }
}
