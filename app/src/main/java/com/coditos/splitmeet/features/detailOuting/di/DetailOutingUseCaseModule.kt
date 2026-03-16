package com.coditos.splitmeet.features.detailOuting.di

import com.coditos.splitmeet.features.detailOuting.domain.repositories.DetailOutingRepository
import com.coditos.splitmeet.features.detailOuting.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

import com.coditos.splitmeet.core.session.domain.usecases.GetUserIdUseCase

@Module
@InstallIn(SingletonComponent::class)
object DetailOutingUseCaseModule {

    @Provides
    @Singleton
    fun provideGetOutingDetailUseCase(repository: DetailOutingRepository): GetOutingDetailUseCase {
        return GetOutingDetailUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetParticipantsUseCase(repository: DetailOutingRepository): GetParticipantsUseCase {
        return GetParticipantsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetOutingItemsUseCase(repository: DetailOutingRepository): GetOutingItemsUseCase {
        return GetOutingItemsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSearchUsersUseCase(repository: DetailOutingRepository): SearchUsersUseCase {
        return SearchUsersUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddParticipantUseCase(repository: DetailOutingRepository): AddParticipantUseCase {
        return AddParticipantUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideConfirmPaymentUseCase(repository: DetailOutingRepository): ConfirmPaymentUseCase {
        return ConfirmPaymentUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideConfirmParticipantPaymentUseCase(repository: DetailOutingRepository): ConfirmParticipantPaymentUseCase {
        return ConfirmParticipantPaymentUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRemoveParticipantUseCase(repository: DetailOutingRepository): RemoveParticipantUseCase {
        return RemoveParticipantUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateOutingUseCase(repository: DetailOutingRepository): UpdateOutingUseCase {
        return UpdateOutingUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteOutingUseCase(repository: DetailOutingRepository): DeleteOutingUseCase {
        return DeleteOutingUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetCategoriesUseCase(repository: DetailOutingRepository): GetCategoriesUseCase {
        return GetCategoriesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGenerateOutingQrUseCase(): GenerateOutingQrUseCase {
        return GenerateOutingQrUseCase()
    }

    @Provides
    @Singleton
    fun provideJoinOutingUseCase(repository: DetailOutingRepository): JoinOutingUseCase {
        return JoinOutingUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetPaymentsByOutingUseCase(repository: DetailOutingRepository): GetPaymentsByOutingUseCase {
        return GetPaymentsByOutingUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDetailOutingUseCases(
        getOutingDetail: GetOutingDetailUseCase,
        getParticipants: GetParticipantsUseCase,
        getOutingItems: GetOutingItemsUseCase,
        searchUsers: SearchUsersUseCase,
        addParticipant: AddParticipantUseCase,
        confirmPayment: ConfirmPaymentUseCase,
        confirmParticipantPayment: ConfirmParticipantPaymentUseCase,
        removeParticipant: RemoveParticipantUseCase,
        updateOuting: UpdateOutingUseCase,
        deleteOuting: DeleteOutingUseCase,
        getCategories: GetCategoriesUseCase,
        getUserId: GetUserIdUseCase,
        generateOutingQr: GenerateOutingQrUseCase,
        joinOuting: JoinOutingUseCase,
        getPayments: GetPaymentsByOutingUseCase
    ): DetailOutingUseCases {
        return DetailOutingUseCases(
            getOutingDetail = getOutingDetail,
            getParticipants = getParticipants,
            getOutingItems = getOutingItems,
            searchUsers = searchUsers,
            addParticipant = addParticipant,
            confirmPayment = confirmPayment,
            confirmParticipantPayment = confirmParticipantPayment,
            removeParticipant = removeParticipant,
            updateOuting = updateOuting,
            deleteOuting = deleteOuting,
            getCategories = getCategories,
            getUserId = getUserId,
            generateOutingQr = generateOutingQr,
            joinOuting = joinOuting,
            getPayments = getPayments
        )
    }
}
