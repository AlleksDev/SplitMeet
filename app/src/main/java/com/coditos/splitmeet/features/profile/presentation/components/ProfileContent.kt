package com.coditos.splitmeet.features.profile.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coditos.splitmeet.features.profile.domain.entities.UserProfile

@Composable
fun ProfileContent(
    profile: UserProfile,
    onLogout: () -> Unit,
    onEdit: () -> Unit
) {
    val initials = profile.name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        ProfileAvatar(initials = initials)

        Spacer(modifier = Modifier.height(24.dp))

        ProfileInfoCard(profile = profile)

        Spacer(modifier = Modifier.height(24.dp))

        ProfileActionButtons(
            onEdit = onEdit,
            onLogout = onLogout
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
