package com.coditos.splitmeet.features.home.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coditos.splitmeet.core.ui.theme.successGreen


@Composable
fun ExpenseInfoRow(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            fontWeight = FontWeight.Black,
            fontSize = 24.sp,
            style = MaterialTheme.typography.titleMedium,
            color = valueColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrevExpenseInfoRow(){
    ExpenseInfoRow("Total", "$100", successGreen, modifier = Modifier)
}