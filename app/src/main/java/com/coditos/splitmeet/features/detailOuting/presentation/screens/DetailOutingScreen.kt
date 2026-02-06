package com.coditos.splitmeet.features.detailOuting.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.coditos.splitmeet.features.detailOuting.presentation.components.AddParticipantModal
import com.coditos.splitmeet.features.detailOuting.presentation.components.ConsumptionSection
import com.coditos.splitmeet.features.detailOuting.presentation.components.OutingInfoCard
import com.coditos.splitmeet.features.detailOuting.presentation.components.ParticipantsSection
import com.coditos.splitmeet.features.detailOuting.presentation.viewmodels.DetailOutingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailOutingScreen(
    outingId: Long,
    viewModel: DetailOutingViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAllProducts: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // Load data when screen is displayed
    LaunchedEffect(outingId) {
        viewModel.loadOutingDetail(outingId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Logo
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    color = Color(0xFFE67E22),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "S",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "Detalles de salida",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading && uiState.outingDetail == null -> {
                    // Loading state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFFE67E22)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Cargando detalles...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }

                uiState.error != null && uiState.outingDetail == null -> {
                    // Error state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = Color.LightGray,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Error al cargar",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.error ?: "Error desconocido",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                uiState.outingDetail != null -> {
                    // Content
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Outing info card
                        OutingInfoCard(
                            outingDetail = uiState.outingDetail!!,
                            participantCount = uiState.participants.size,
                            amountPerPerson = uiState.amountPerPerson,
                            onEditClick = { /* TODO: Navigate to edit */ }
                        )

                        // Consumption section (max 3 items)
                        ConsumptionSection(
                            items = uiState.items,
                            displayedItems = uiState.displayedItems,
                            hasMoreItems = uiState.hasMoreItems,
                            total = uiState.outingDetail!!.totalAmount,
                            onAddItemClick = { /* TODO: Navigate to add item (product feature) */ },
                            onViewAllClick = { onNavigateToAllProducts(outingId) }
                        )

                        // Participants section
                        ParticipantsSection(
                            participants = uiState.participants,
                            paidCount = uiState.paidParticipantsCount,
                            totalCount = uiState.participants.size,
                            onAddParticipantClick = { viewModel.showAddParticipantModal() },
                            onMarkAsPaid = { /* TODO: Mark as paid */ },
                            onRemoveParticipant = { /* TODO: Remove participant */ }
                        )

                        // Bottom spacing
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }

            // Add participant modal
            AddParticipantModal(
                isVisible = uiState.showAddParticipantModal,
                searchQuery = uiState.searchQuery,
                searchResults = uiState.searchResults,
                isLoading = uiState.isSearching,
                error = uiState.addParticipantError,
                addingUserId = uiState.addingParticipantId,
                onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
                onAddParticipant = { user ->
                    viewModel.addParticipant(user.id)
                },
                onDismiss = { viewModel.hideAddParticipantModal() }
            )
        }
    }
}
