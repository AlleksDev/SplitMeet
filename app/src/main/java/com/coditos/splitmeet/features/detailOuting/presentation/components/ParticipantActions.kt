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
import androidx.compose.ui.graphics.Color
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
        modifier = modifier
            .height(40.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.5.dp,
            color = if (isRemoving) colors.error.copy(alpha = 0.5f) else colors.error
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = if (isRemoving) colors.error.copy(alpha = 0.5f) else colors.error,
            disabledContentColor = colors.error.copy(alpha = 0.4f)
        )
    ) {
        if (isRemoving) {
            CircularProgressIndicator(
                modifier = Modifier.size(14.dp),
                strokeWidth = 1.5.dp,
                color = colors.error.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Eliminando...",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Eliminar participante",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Eliminar",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
fun ConfirmarPagoButton(
    isConfirming: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val successColor = Color(0xFF4CAF50)

    FilledTonalButton(
        onClick = onClick,
        enabled = !isConfirming,
        modifier = modifier
            .height(40.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = successColor.copy(alpha = 0.2f),
            contentColor = successColor,
            disabledContainerColor = successColor.copy(alpha = 0.1f),
            disabledContentColor = successColor.copy(alpha = 0.4f)
        )
    ) {
        if (isConfirming) {
            CircularProgressIndicator(
                modifier = Modifier.size(14.dp),
                strokeWidth = 1.5.dp,
                color = successColor
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Confirmando...",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "Confirmar pago",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Confirmar pago",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
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
