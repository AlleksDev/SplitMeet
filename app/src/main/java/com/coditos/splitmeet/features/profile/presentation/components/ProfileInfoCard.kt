package com.coditos.splitmeet.features.profile.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coditos.splitmeet.features.profile.domain.entities.UserProfile

@Composable
fun ProfileInfoCard(
    profile: UserProfile
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ProfileInfoRow(
            icon = Icons.Outlined.AccountCircle,
            label = "Nombre:",
            value = profile.name
        )
        ProfileInfoRow(
            icon = Icons.Outlined.Sell,
            label = "Nombre de usuario:",
            value = "@${profile.username}"
        )
        ProfileInfoRow(
            icon = Icons.Outlined.Phone,
            label = "Teléfono:",
            value = profile.phone.ifBlank { "Sin teléfono" }
        )
        ProfileInfoRow(
            icon = Icons.Outlined.Email,
            label = "Correo electrónico:",
            value = profile.email
        )
        ProfileInfoRow(
            icon = Icons.Outlined.Lock,
            label = "Contraseña:",
            value = "************"
        )
    }
}
