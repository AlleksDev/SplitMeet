package com.coditos.splitmeet.features.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileAvatar(
    initials: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(160.dp)
            .background(MaterialTheme.colorScheme.primary, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = Color.White,
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
