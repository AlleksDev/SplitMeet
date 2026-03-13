package com.coditos.splitmeet.features.notification.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coditos.splitmeet.features.notification.domain.entities.Notification
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

@Composable
fun NotificationListContent(
    notifications: List<Notification>,
    respondingIds: Set<Long>,
    acceptedIds: Set<Long>,
    rejectedIds: Set<Long>,
    onAccept: (Notification) -> Unit,
    onReject: (Notification) -> Unit
) {
    val grouped = notifications.groupBy { it.dateLabel() }
        .toSortedMap(compareByDescending { it })

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        grouped.forEach { (date, groupedNotifications) ->
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
                items = groupedNotifications,
                key = { it.id }
            ) { notification ->
                NotificationCard(
                    notification = notification,
                    isResponding = notification.id in respondingIds,
                    localAccepted = notification.id in acceptedIds,
                    localRejected = notification.id in rejectedIds,
                    onAccept = { onAccept(notification) },
                    onReject = { onReject(notification) }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
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
