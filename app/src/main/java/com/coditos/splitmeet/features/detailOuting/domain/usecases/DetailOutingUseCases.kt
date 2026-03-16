package com.coditos.splitmeet.features.detailOuting.domain.usecases

import com.coditos.splitmeet.core.session.domain.usecases.GetUserIdUseCase

data class DetailOutingUseCases(
    val getOutingDetail: GetOutingDetailUseCase,
    val getParticipants: GetParticipantsUseCase,
    val getOutingItems: GetOutingItemsUseCase,
    val searchUsers: SearchUsersUseCase,
    val addParticipant: AddParticipantUseCase,
    val confirmPayment: ConfirmPaymentUseCase,
    val confirmParticipantPayment: ConfirmParticipantPaymentUseCase,
    val removeParticipant: RemoveParticipantUseCase,
    val updateOuting: UpdateOutingUseCase,
    val deleteOuting: DeleteOutingUseCase,
    val getCategories: GetCategoriesUseCase,
    val getUserId: GetUserIdUseCase
)
