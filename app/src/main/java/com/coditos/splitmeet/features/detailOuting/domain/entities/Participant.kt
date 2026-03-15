package com.coditos.splitmeet.features.detailOuting.domain.entities

data class Participant(
    val id: Long,
    val outingId: Long,
    val userId: Long,
    val username: String,
    val name: String,
    val status: String,
    val paymentId: Long?,
    val paymentStatus: String?,
    val amountOwed: Double,
    val customAmount: Double?
) {
    val isConfirmed: Boolean
        get() = status.equals("confirmed", ignoreCase = true)
    
    val isPending: Boolean
        get() = status.equals("pending", ignoreCase = true)
    
    val isPaid: Boolean
        get() = paymentStatus.equals("paid", ignoreCase = true) || status.equals("paid", ignoreCase = true)

    val isPaymentPending: Boolean
        get() = when {
            paymentStatus.equals("pending", ignoreCase = true) -> true
            paymentStatus == null && isConfirmed -> true
            else -> false
        }
    
    val isDeclined: Boolean
        get() = status.equals("declined", ignoreCase = true)
    
    val displayInitial: Char
        get() = name.firstOrNull()?.uppercaseChar() ?: username.firstOrNull()?.uppercaseChar() ?: '?'
}
