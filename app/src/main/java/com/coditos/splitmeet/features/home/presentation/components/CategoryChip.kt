package com.coditos.splitmeet.features.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryChip(
    category: String,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, contentColor) = when (category.lowercase()) {
        "restaurante" -> Pair(Color(0xFFE8F5E9), Color(0xFF4CAF50))
        "cine" -> Pair(Color(0xFFE3F2FD), Color(0xFF2196F3))
        else -> Pair(Color(0xFFF5F5F5), Color(0xFF757575))
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .border(1.dp, contentColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = category,
            color = contentColor,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp)
        )
    }
}
