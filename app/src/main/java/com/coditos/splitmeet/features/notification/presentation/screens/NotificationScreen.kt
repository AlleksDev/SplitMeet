package com.coditos.splitmeet.features.notification.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coditos.splitmeet.features.notification.domain.entities.Notification
import com.coditos.splitmeet.features.notification.domain.entities.NotificationType
import com.coditos.splitmeet.features.notification.presentation.viewmodels.NotificationViewModel
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Notificaciones",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary,
            thickness = 2.dp
        )

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            uiState.notifications.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tienes notificaciones",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            else -> {
                val grouped = uiState.notifications.groupBy { it.dateLabel() }
                    .toSortedMap(compareByDescending { it })

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    grouped.forEach { (date, notifications) ->
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = date.formatAsHeader(),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                ),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        items(
                            items = notifications,
                            key = { it.id }
                        ) { notification ->
                            NotificationCard(
                                notification = notification,
                                isResponding = notification.id in uiState.respondingIds,
                                isAccepted = notification.id in uiState.acceptedIds,
                                isRejected = notification.id in uiState.rejectedIds,
                                onAccept = { viewModel.respondToInvitation(notification, true) },
                                onReject = { viewModel.respondToInvitation(notification, false) }
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

// ── Card ─────────────────────────────────────────────────────────────────────

@Composable
private fun NotificationCard(
    notification: Notification,
    isResponding: Boolean,
    isAccepted: Boolean,
    isRejected: Boolean,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    val borderColor = if (notification.isPending && !isAccepted && !isRejected) {
        Color(0xFFFF9500) // brand orange for pending invitations
    } else {
        Color(0xFFE0E0E0) // neutral for others
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Message text
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = buildNotificationMessage(notification),
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Action area
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
                        color = Color(0xFF9E9E9E)
                    )
                }

                isRejected -> {
                    Text(
                        text = "Rechazado",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFE57373)
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
                                tint = Color(0xFFE57373)
                            )
                        }
                    }
                }

                notification.type == NotificationType.INVITATION_ACCEPTED -> {
                    Text(
                        text = "Aceptado",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9E9E9E)
                    )
                }

                notification.type == NotificationType.INVITATION_REJECTED -> {
                    Text(
                        text = "Rechazado",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFE57373)
                    )
                }
            }
        }
    }
}

// ── Helpers ──────────────────────────────────────────────────────────────────

@Composable
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

private val headerFormatter = DateTimeFormatter.ofPattern("dd 'de' MMMM", Locale("es"))

private fun Notification.dateLabel(): LocalDate {
    return try {
        OffsetDateTime.parse(createdAt).toLocalDate()
    } catch (_: DateTimeParseException) {
        try {
            LocalDate.parse(createdAt.take(10))
        } catch (_: DateTimeParseException) {
            LocalDate.now()
        }
    }
}

private fun LocalDate.formatAsHeader(): String = format(headerFormatter)
