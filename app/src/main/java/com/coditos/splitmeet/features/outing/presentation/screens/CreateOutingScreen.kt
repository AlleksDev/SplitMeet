package com.coditos.splitmeet.features.outing.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coditos.splitmeet.features.outing.presentation.components.OutingButton
import com.coditos.splitmeet.features.outing.presentation.components.OutingDatePicker
import com.coditos.splitmeet.features.outing.presentation.components.OutingDescriptionField
import com.coditos.splitmeet.features.outing.presentation.components.OutingDropdown
import com.coditos.splitmeet.features.outing.presentation.components.OutingHeader
import com.coditos.splitmeet.features.outing.presentation.components.OutingTextField
import com.coditos.splitmeet.features.outing.presentation.viewmodels.OutingViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CreateOutingScreen(
    viewModel: OutingViewModel,
    modifier: Modifier = Modifier,
    onOutingCreated: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    // Set default date to today when screen loads
    LaunchedEffect(Unit) {
        if (uiState.selectedDate.isBlank()) {
            val apiFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = apiFormat.format(Calendar.getInstance().time)
            viewModel.onDateSelected(today)
        }
    }

    // Handle success
    LaunchedEffect(uiState.isSuccess, uiState.createdOutingId) {
        if (uiState.isSuccess && uiState.createdOutingId != null) {
            onOutingCreated(uiState.createdOutingId!!)
            viewModel.clearState()
        }
    }

    // Handle error
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                OutingHeader(title = "Nueva salida")

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color(0xFFE0E0E0)
                )

                // Form content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                        .imePadding(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Nombre de la salida
                    OutingTextField(
                        value = uiState.name,
                        onValueChange = viewModel::onNameChanged,
                        label = "Nombre de la salida",
                        placeholder = "Ej. Cena con amigos",
                        isError = uiState.nameError != null,
                        errorMessage = uiState.nameError,
                        enabled = !uiState.isLoading
                    )

                    // Fecha
                    OutingDatePicker(
                        selectedDate = uiState.selectedDate,
                        onDateSelected = viewModel::onDateSelected,
                        label = "Fecha",
                        placeholder = "DD/MM/AAAA",
                        isError = uiState.dateError != null,
                        errorMessage = uiState.dateError,
                        enabled = !uiState.isLoading
                    )

                    // Tipo de salida (Categoría)
                    OutingDropdown(
                        selectedValue = uiState.selectedCategory,
                        onValueSelected = viewModel::onCategorySelected,
                        options = uiState.categories,
                        label = "Tipo de salida",
                        placeholder = "Seleccionar una opción",
                        displayText = { it.name },
                        isError = uiState.categoryError != null,
                        errorMessage = uiState.categoryError,
                        enabled = !uiState.isLoading,
                        isLoading = uiState.isCategoriesLoading
                    )

                    // Tipo de cálculo (Split Type)
                    OutingDropdown(
                        selectedValue = uiState.selectedSplitType,
                        onValueSelected = viewModel::onSplitTypeSelected,
                        options = uiState.splitTypes,
                        label = "Tipo de cálculo",
                        placeholder = "Seleccionar una opción",
                        displayText = { it.displayName },
                        isError = uiState.splitTypeError != null,
                        errorMessage = uiState.splitTypeError,
                        enabled = !uiState.isLoading
                    )

                    // Descripción
                    OutingDescriptionField(
                        value = uiState.description,
                        onValueChange = viewModel::onDescriptionChanged,
                        label = "Descripción",
                        placeholder = "Ej. Cena de reencuentro con amigos para ponernos al día y recordar viejos tiempos",
                        enabled = !uiState.isLoading
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Button at the bottom
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    OutingButton(
                        text = "Crear salida",
                        onClick = viewModel::createOuting,
                        isLoading = uiState.isLoading,
                        enabled = uiState.isFormValid
                    )
                }
            }
        }
    }
}
