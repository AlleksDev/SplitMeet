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
    
    /**
     * A participant is considered PAID if:
     * 1. paymentStatus is "paid" OR
     * 2. status is "paid" OR
     * 3. paymentStatus is "confirmed" (some APIs use this)
     * 
     * This is a read-only state - once paid, no actions can be performed
     */
    val isPaid: Boolean
        get() {
            if (paymentStatus != null) {
                return paymentStatus.equals("paid", ignoreCase = true) || 
                       paymentStatus.equals("confirmed", ignoreCase = true)
            }
            return status.equals("paid", ignoreCase = true)
        }

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
