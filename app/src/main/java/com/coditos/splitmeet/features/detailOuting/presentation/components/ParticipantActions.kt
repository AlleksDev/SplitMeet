package com.coditos.splitmeet.features.detailOuting.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun EliminarParticipanteButton(
    isRemoving: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    OutlinedButton(
        onClick = onClick,
        enabled = !isRemoving,
        modifier = modifier.height(38.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, colors.errorContainer),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = colors.errorContainer,
            disabledContainerColor = colors.errorContainer.copy(alpha = 0.5f),
            disabledContentColor = colors.onErrorContainer.copy(alpha = 0.4f)
        )
    ) {
        if (isRemoving) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp,
                color = colors.onErrorContainer
            )
            Spacer(modifier = Modifier.width(6.dp))
        } else {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = "Eliminar",
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ConfirmarPagoButton(
    isConfirming: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    FilledTonalButton(
        onClick = onClick,
        enabled = !isConfirming,
        modifier = modifier.height(38.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = colors.primaryContainer,
            contentColor = colors.onPrimaryContainer,
            disabledContainerColor = colors.primaryContainer.copy(alpha = 0.5f),
            disabledContentColor = colors.onPrimaryContainer.copy(alpha = 0.4f)
        )
    ) {
        if (isConfirming) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp,
                color = colors.onPrimaryContainer
            )
            Spacer(modifier = Modifier.width(6.dp))
        } else {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = "Confirmar pago",
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ParticipantActions(
    showConfirmPayment: Boolean,
    showRemove: Boolean,
    isConfirmingPayment: Boolean,
    isRemoving: Boolean,
    onConfirmPayment: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!showConfirmPayment && !showRemove) return

    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        if (showRemove) {
            EliminarParticipanteButton(
                isRemoving = isRemoving,
                onClick = onRemove
            )
            if (showConfirmPayment) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        if (showConfirmPayment) {
            ConfirmarPagoButton(
                isConfirming = isConfirmingPayment,
                onClick = onConfirmPayment,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
