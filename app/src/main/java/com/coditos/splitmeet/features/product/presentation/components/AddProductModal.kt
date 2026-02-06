package com.coditos.splitmeet.features.product.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.coditos.splitmeet.features.product.domain.entities.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductModal(
    isVisible: Boolean,
    selectedProduct: Product?,
    productName: String,
    productPresentation: String,
    productQuantity: Int,
    productPrice: String,
    isLoading: Boolean,
    canAdd: Boolean,
    onProductNameChanged: (String) -> Unit,
    onProductPresentationChanged: (String) -> Unit,
    onProductQuantityChanged: (Int) -> Unit,
    onProductPriceChanged: (String) -> Unit,
    onAddClick: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!isVisible) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { if (!isLoading) onDismiss() },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            // Name field
            Text(
                text = "Nombre del producto",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = productName,
                onValueChange = onProductNameChanged,
                placeholder = { Text("Ej. Agua de tamarindo") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && selectedProduct == null,
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE67E22),
                    focusedLabelColor = Color(0xFFE67E22)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Presentation field
            Text(
                text = "Presentación",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = productPresentation,
                onValueChange = onProductPresentationChanged,
                placeholder = { Text("Ej. Jarra") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && selectedProduct == null,
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE67E22),
                    focusedLabelColor = Color(0xFFE67E22)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Quantity and Price row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Quantity field
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Cantidad",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = productQuantity.toString(),
                        onValueChange = { value ->
                            val qty = value.toIntOrNull()
                            if (qty != null && qty >= 1) {
                                onProductQuantityChanged(qty)
                            } else if (value.isEmpty()) {
                                onProductQuantityChanged(1)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFE67E22),
                            focusedLabelColor = Color(0xFFE67E22)
                        )
                    )
                }

                // Price field
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Precio",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = productPrice,
                        onValueChange = onProductPriceChanged,
                        placeholder = { Text("$199.99") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        leadingIcon = {
                            Text(
                                text = "$",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFE67E22),
                            focusedLabelColor = Color(0xFFE67E22)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Add button
            Button(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && canAdd,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE67E22),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = if (isLoading) "Agregando..." else "Agregar",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}
