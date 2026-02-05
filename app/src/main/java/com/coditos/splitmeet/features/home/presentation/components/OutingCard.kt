package com.coditos.splitmeet.features.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coditos.splitmeet.core.ui.theme.SplitMeetTheme

data class ExpenseUiModel(
    val title: String,
    val category: String,
    val total: String,
    val perPerson: String,
    val attendees: Float,
    val paid: Float
)


@Composable
fun OutingCard(expense: ExpenseUiModel) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = expense.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                //CategoryChip(expense.category)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "12 de febrero • ${expense.attendees} asistentes",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                ExpenseInfoRow(
                    label = "Total",
                    value = "$${expense.total}",
                    valueColor = Color(0xFF66BB6A),
                    modifier = Modifier.weight(1f)
                )

                ExpenseInfoRow(
                    label = "Por persona",
                    value = "$${expense.perPerson}",
                    valueColor = Color(0xFF1E88E5),
                    modifier = Modifier.weight(1f)
                )

                CircleProgress(
                    value = expense.paid,
                    total = expense.attendees
                )
            }
        }
    }
}

@Composable
fun CategoryChip(x0: String) {
    TODO("Not yet implemented")
}

@Preview(showBackground = true)
@Composable
fun PrevOutingCard() {
    SplitMeetTheme {
        val exp = ExpenseUiModel(
            title = "IDita",
            category = "Cine",
            total = "900",
            perPerson = "100",
            paid = 3f,
            attendees = 4f
        )
        OutingCard(exp)
    }
}
