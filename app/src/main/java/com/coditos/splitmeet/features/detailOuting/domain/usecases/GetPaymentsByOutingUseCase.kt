package com.coditos.splitmeet.features.detailOuting.domain.usecases

import com.coditos.splitmeet.features.detailOuting.domain.repositories.DetailOutingRepository
import javax.inject.Inject

class GetPaymentsByOutingUseCase @Inject constructor(
    private val repository: DetailOutingRepository
) {
    suspend operator fun invoke(outingId: Long): Result<List<PaymentData>> {
        return try {
            val payments = repository.getPaymentsByOuting(outingId)
            Result.success(payments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Data class representing payment information for a participant
 * Used to map payment status back to participants
 */
data class PaymentData(
    val id: Long,
    val participantId: Long,
    val status: String, // "pending", "paid", "cancelled", etc.
    val amount: Double
)
