package com.coditos.splitmeet.features.group.presentation.screens.components

import androidx.compose.ui.graphics.Color

val avatarColors = listOf(
    Color(0xFF2196F3),
    Color(0xFFE91E63),
    Color(0xFF9C27B0),
    Color(0xFF4CAF50),
    Color(0xFFFF9800),
    Color(0xFF00BCD4),
    Color(0xFF795548),
    Color(0xFFAD1457)
)

fun getAvatarColor(id: Long): Color = avatarColors[(id % avatarColors.size).toInt()]
