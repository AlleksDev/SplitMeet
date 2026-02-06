package com.coditos.splitmeet.features.detailOuting.domain.entities

data class Participant(
    val id: Long,
    val outingId: Long,
    val userId: Long,
    val username: String,
    val name: String,
    val status: String,
    val amountOwed: Double,
    val customAmount: Double?,
    val paymentStatus: String
) {
    val isPaid: Boolean
        get() = paymentStatus.equals("paid", ignoreCase = true)
    
    val isPending: Boolean
        get() = paymentStatus.equals("pending", ignoreCase = true)
    
    val displayInitial: Char
        get() = name.firstOrNull()?.uppercaseChar() ?: username.firstOrNull()?.uppercaseChar() ?: '?'
}
