package com.coditos.splitmeet.features.detailOuting.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingDetail
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun OutingInfoCard(
    outingDetail: OutingDetail,
    participantCount: Int,
    amountPerPerson: Double,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {}
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
    
    // Format date
    val formattedDate = try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d 'de' MMMM", Locale("es", "ES"))
        val date = inputFormat.parse(outingDetail.outingDate)
        date?.let { outputFormat.format(it) } ?: outingDetail.outingDate
    } catch (e: Exception) {
        outingDetail.outingDate
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title and edit button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = outingDetail.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.weight(1f)
                )
                
                if (outingDetail.isEditable) {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tags (Category and Status)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Category tag
                outingDetail.categoryName?.let { category ->
                    Text(
                        text = "🍽️ $category",
                        modifier = Modifier
                            .background(
                                color = Color(0xFFFFF3E0),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFE65100)
                    )
                }

                // Status tag
                val statusText = when (outingDetail.status.lowercase()) {
                    "active" -> "Pendiente"
                    "completed" -> "Completado"
                    "cancelled" -> "Cancelado"
                    else -> outingDetail.status
                }
                val statusColor = when (outingDetail.status.lowercase()) {
                    "active" -> Color(0xFFF5F5F5)
                    "completed" -> Color(0xFFE8F5E9)
                    "cancelled" -> Color(0xFFFFEBEE)
                    else -> Color(0xFFF5F5F5)
                }
                val statusTextColor = when (outingDetail.status.lowercase()) {
                    "active" -> Color.Gray
                    "completed" -> Color(0xFF2E7D32)
                    "cancelled" -> Color(0xFFC62828)
                    else -> Color.Gray
                }

                Text(
                    text = "⏱ $statusText",
                    modifier = Modifier
                        .background(
                            color = statusColor,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = statusTextColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Date and participants
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "📅 $formattedDate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = "👥 $participantCount asistentes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            // Description
            outingDetail.description?.let { description ->
                if (description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Totals row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Total
                Column {
                    Text(
                        text = "Cuenta total",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = currencyFormat.format(outingDetail.totalAmount),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black
                    )
                }

                // Per person
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Por persona",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = currencyFormat.format(amountPerPerson),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFFE67E22)
                    )
                }
            }
        }
    }
}
