package com.coditos.splitmeet.features.detailOuting.presentation.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.coditos.splitmeet.features.detailOuting.domain.entities.Participant
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ParticipantsSection(
    participants: List<Participant>,
    paidCount: Int,
    totalCount: Int,
    canRemoveParticipants: Boolean,
    isCreator: Boolean,
    selectedParticipantId: Long?,
    confirmingPaymentUserId: Long?,
    removingParticipantUserId: Long?,
    modifier: Modifier = Modifier,
    onParticipantClick: (Long) -> Unit = {},
    onAddParticipantClick: () -> Unit = {},
    onMarkAsPaid: (Participant) -> Unit = {},
    onRemoveParticipant: (Participant) -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Participantes",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Text(
                    text = "$paidCount/$totalCount pagaron",
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (participants.isEmpty()) {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No hay participantes agregados",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Participants list
                participants.forEach { participant ->
                    val isSelected = selectedParticipantId == participant.userId

                    ParticipantItem(
                        participant = participant,
                        isSelected = isSelected,
                        isCreator = isCreator,
                        canRemove = canRemoveParticipants,
                        isConfirmingPayment = confirmingPaymentUserId == participant.userId,
                        isRemoving = removingParticipantUserId == participant.userId,
                        onClick = { onParticipantClick(participant.userId) },
                        onMarkAsPaid = { onMarkAsPaid(participant) },
                        onRemove = { onRemoveParticipant(participant) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Add participant button
            Button(
                onClick = onAddParticipantClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Agregar otra persona",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Composable
fun ParticipantItem(
    participant: Participant,
    isSelected: Boolean,
    isCreator: Boolean,
    canRemove: Boolean,
    isConfirmingPayment: Boolean,
    isRemoving: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onMarkAsPaid: () -> Unit = {},
    onRemove: () -> Unit = {}
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "MX"))

    val avatarColors = listOf(
        Color(0xFF5C6BC0),
        Color(0xFF26A69A),
        Color(0xFFEF5350),
        Color(0xFFAB47BC),
        Color(0xFF42A5F5),
        Color(0xFFFF7043)
    )
    val avatarColor = avatarColors[participant.id.hashCode().mod(avatarColors.size).let { if (it < 0) -it else it }]

    val selectedBorderColor = Color(0xFFE67E22)
    val successColor = MaterialTheme.colorScheme.secondary  // Using tertiary as success-like color
    // MUCH MORE VISIBLE background for paid participants
    val paidBackgroundColor = successColor.copy(alpha = 0.25f)  // Changed from 0.08f to 0.25f

    // Non-paid participants can be selected and clicked; paid participants are read-only
    val canBeSelected = !participant.isPaid && isCreator
    val shouldShowBorder = isSelected || participant.isPaid
    val borderColor = when {
        participant.isPaid -> successColor
        isSelected -> selectedBorderColor
        else -> Color.Transparent
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Participant card row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (shouldShowBorder) {
                        Modifier.border(
                            width = 2.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(12.dp)
                        )
                    } else {
                        Modifier
                    }
                )
                .background(
                    color = if (participant.isPaid) paidBackgroundColor else MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(12.dp)
                )
                // ONLY allow clicks for non-paid participants when creator
                .then(
                    if (canBeSelected) {
                        Modifier.clickable(
                            enabled = true,
                            onClick = onClick
                        )
                    } else {
                        // Paid participants: completely non-interactive
                        Modifier
                    }
                )
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(avatarColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = participant.displayInitial.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Name and username
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = participant.name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                Text(
                    text = "@${participant.username}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Amount and status
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = currencyFormat.format(participant.amountOwed),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (participant.isPaid) successColor else MaterialTheme.colorScheme.onSurface
                )

                Log.d("ParticipantItem", "Status: ${participant.paymentStatus}")
                val statusText = when {
                    participant.isPaid -> "Pagado"
                    participant.isConfirmed -> "Confirmado"
                    participant.isPending -> "Pendiente"
                    participant.isDeclined -> "Rechazado"
                    else -> participant.status
                }
                val statusColor = when {
                    participant.isPaid -> successColor
                    participant.isConfirmed -> MaterialTheme.colorScheme.secondary
                    participant.isPending -> MaterialTheme.colorScheme.tertiary
                    participant.isDeclined -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (participant.isPaid) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(18.dp)
                        )
                    } else if (participant.isConfirmed) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodySmall,
                        color = statusColor,
                        fontWeight = if (participant.isPaid) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        // Action buttons - revealed on selection (creator only, but NEVER for paid participants)
        AnimatedVisibility(
            visible = isSelected && isCreator && !participant.isPaid,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            ParticipantActions(
                showConfirmPayment = participant.isPaymentPending,
                showRemove = canRemove,
                isConfirmingPayment = isConfirmingPayment,
                isRemoving = isRemoving,
                onConfirmPayment = onMarkAsPaid,
                onRemove = onRemove,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
