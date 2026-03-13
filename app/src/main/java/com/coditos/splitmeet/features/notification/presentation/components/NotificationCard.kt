package com.coditos.splitmeet.features.notification.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coditos.splitmeet.features.notification.domain.entities.Notification
import com.coditos.splitmeet.features.notification.domain.entities.NotificationType

@Composable
fun NotificationCard(
    notification: Notification,
    isResponding: Boolean,
    localAccepted: Boolean,
    localRejected: Boolean,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    val isAccepted = notification.isAccepted || localAccepted
    val isRejected = notification.isRejected || localRejected

    val borderColor = if (notification.isPending && !isAccepted && !isRejected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = buildNotificationMessage(notification),
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            when {
                isResponding -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                isAccepted -> {
                    Text(
                        text = "Aceptado",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                isRejected -> {
                    Text(
                        text = "Rechazado",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                notification.isPending -> {
                    Row {
                        IconButton(onClick = onAccept, modifier = Modifier.size(36.dp)) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Aceptar",
                                tint = Color(0xFF4CAF50)
                            )
                        }
                        IconButton(onClick = onReject, modifier = Modifier.size(36.dp)) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Rechazar",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                notification.type == NotificationType.INVITATION_ACCEPTED -> {
                    Text(
                        text = "Aceptado",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                notification.type == NotificationType.INVITATION_REJECTED -> {
                    Text(
                        text = "Rechazado",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

private fun buildNotificationMessage(notification: Notification) = buildAnnotatedString {
    val bold = SpanStyle(fontWeight = FontWeight.Bold)

    val inviter = notification.inviterName ?: ""
    val group = notification.groupName ?: ""
    val outing = notification.outingName ?: ""

    when (notification.type) {
        NotificationType.GROUP_INVITATION -> {
            withStyle(bold) { append(inviter) }
            append(" te ha invitado a unirte al grupo ")
            withStyle(bold) { append(group) }
        }

        NotificationType.OUTING_INVITATION -> {
            withStyle(bold) { append(inviter) }
            if (group.isNotEmpty()) {
                append(" te ha invitado a la salida ")
                withStyle(bold) { append(outing) }
                append(" del grupo ")
                withStyle(bold) { append(group) }
            } else {
                append(" te ha invitado a participar en la salida ")
                withStyle(bold) { append(outing) }
            }
        }

        NotificationType.INVITATION_ACCEPTED -> {
            withStyle(bold) { append(inviter) }
            if (outing.isNotEmpty()) {
                append(" te ha invitado a la salida ")
                withStyle(bold) { append(outing) }
                append(" del grupo ")
                withStyle(bold) { append(group) }
            } else {
                append(" aceptó tu invitación al grupo ")
                withStyle(bold) { append(group) }
            }
        }

        NotificationType.INVITATION_REJECTED -> {
            withStyle(bold) { append(inviter) }
            if (outing.isNotEmpty()) {
                append(" te ha invitado a la salida ")
                withStyle(bold) { append(outing) }
                append(" del grupo ")
                withStyle(bold) { append(group) }
            } else {
                append(" rechazó tu invitación al grupo ")
                withStyle(bold) { append(group) }
            }
        }

        NotificationType.UNKNOWN -> {
            append(notification.message)
        }
    }
}
