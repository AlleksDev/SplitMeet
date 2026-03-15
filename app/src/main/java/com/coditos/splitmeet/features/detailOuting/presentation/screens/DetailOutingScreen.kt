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
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coditos.splitmeet.features.detailOuting.presentation.components.AddParticipantModal
import com.coditos.splitmeet.features.detailOuting.presentation.components.ConsumptionSection
import com.coditos.splitmeet.features.detailOuting.presentation.components.DeleteConfirmationDialog
import com.coditos.splitmeet.features.detailOuting.presentation.components.EditOutingModal
import com.coditos.splitmeet.features.detailOuting.presentation.components.OutingInfoCard
import com.coditos.splitmeet.features.detailOuting.presentation.components.ParticipantsSection
import com.coditos.splitmeet.features.detailOuting.presentation.components.RemoveParticipantConfirmationDialog
import com.coditos.splitmeet.features.detailOuting.presentation.viewmodels.DetailOutingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailOutingScreen(
    outingId: Long,
    viewModel: DetailOutingViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToAddProducts: (outingId: Long, categoryId: Long, categoryName: String) -> Unit = { _, _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Load data when screen is displayed
    LaunchedEffect(outingId) {
        viewModel.loadOutingDetail(outingId)
    }

    // Set up delete success callback
    LaunchedEffect(Unit) {
        viewModel.setOnDeleteSuccess {
            onNavigateBack()
        }
    }

    // Show success message in Snackbar
    LaunchedEffect(uiState.showSuccessMessage) {
        if (uiState.showSuccessMessage && uiState.successMessage != null) {
            snackbarHostState.showSnackbar(uiState.successMessage!!)
            viewModel.hideSuccessMessage()
        }
    }

    // Show operation errors in Snackbar
    LaunchedEffect(uiState.error) {
        if (uiState.error != null && uiState.outingDetail != null) {
            snackbarHostState.showSnackbar(uiState.error!!)
            viewModel.clearError()
        }
    }

    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(uiState.requireBiometricAuth) {
        val participant = uiState.requireBiometricAuth
        if (participant != null) {
            android.util.Log.d("BiometricFlow", "requireBiometricAuth triggered for participant: ${participant.username}")
            android.util.Log.d("BiometricFlow", "Context type: ${context.javaClass.name}")

            var ctx: android.content.Context = context
            var activity: androidx.fragment.app.FragmentActivity? = null
            while (ctx is android.content.ContextWrapper) {
                android.util.Log.d("BiometricFlow", "Unwrapping: ${ctx.javaClass.name}")
                if (ctx is androidx.fragment.app.FragmentActivity) {
                    activity = ctx
                    android.util.Log.d("BiometricFlow", "FragmentActivity FOUND: ${ctx.javaClass.name}")
                    break
                }
                ctx = ctx.baseContext
            }

            if (activity != null) {
                android.util.Log.d("BiometricFlow", "Launching BiometricPrompt with activity: ${activity.javaClass.name}")
                viewModel.authenticatePendingPayment(activity, participant)
            } else {
                android.util.Log.e("BiometricFlow", "FragmentActivity NOT FOUND after full unwrap. Last context: ${ctx.javaClass.name}")
                viewModel.onBiometricAuthError("No se pudo obtener el contexto de la actividad.")
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {

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
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background
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
                                onEditClick = { viewModel.showEditModal() },
                                onDeleteClick = { viewModel.showDeleteConfirmation() }
                            )

                            // Consumption section (max 3 items)
                            ConsumptionSection(
                                items = uiState.items,
                                displayedItems = uiState.displayedItems,
                                hasMoreItems = uiState.hasMoreItems,
                                total = uiState.outingDetail!!.totalAmount,
                                onAddItemClick = { 
                                    onNavigateToAddProducts(
                                        outingId,
                                        uiState.outingDetail!!.categoryId ?: 0L,
                                        uiState.outingDetail!!.categoryName ?: ""
                                    )
                                },
                                onViewAllClick = { 
                                    onNavigateToAddProducts(
                                        outingId,
                                        uiState.outingDetail!!.categoryId ?: 0L,
                                        uiState.outingDetail!!.categoryName ?: ""
                                    )
                                }
                            )

                            // Participants section
                            ParticipantsSection(
                                participants = uiState.participants,
                                paidCount = uiState.paidParticipantsCount,
                                totalCount = uiState.participants.size,
                                canRemoveParticipants = uiState.outingDetail?.isEditable == true,
                                isCreator = uiState.isCreator,
                                selectedParticipantId = uiState.selectedParticipantId,
                                confirmingPaymentUserId = uiState.confirmingPaymentUserId,
                                removingParticipantUserId = uiState.removingParticipantUserId,
                                onParticipantClick = { viewModel.selectParticipant(it) },
                                onAddParticipantClick = { viewModel.showAddParticipantModal() },
                                onMarkAsPaid = { participant -> viewModel.confirmPayment(participant) },
                                onRemoveParticipant = { participant -> viewModel.requestRemoveParticipant(participant) }
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

                // Edit outing modal
                EditOutingModal(
                    isVisible = uiState.showEditModal,
                    name = uiState.editName,
                    description = uiState.editDescription,
                    categoryId = uiState.editCategoryId,
                    outingDate = uiState.editOutingDate,
                    splitType = uiState.editSplitType,
                    categories = uiState.categories,
                    isUpdating = uiState.isUpdating,
                    onNameChanged = { viewModel.onEditNameChanged(it) },
                    onDescriptionChanged = { viewModel.onEditDescriptionChanged(it) },
                    onCategoryChanged = { viewModel.onEditCategoryChanged(it) },
                    onDateChanged = { viewModel.onEditDateChanged(it) },
                    onSplitTypeChanged = { viewModel.onEditSplitTypeChanged(it) },
                    onSave = { viewModel.updateOuting() },
                    onDismiss = { viewModel.hideEditModal() }
                )

                // Delete confirmation dialog
                DeleteConfirmationDialog(
                    isVisible = uiState.showDeleteConfirmation,
                    outingName = uiState.outingDetail?.name ?: "",
                    isDeleting = uiState.isDeleting,
                    onConfirm = { viewModel.deleteOuting() },
                    onDismiss = { viewModel.hideDeleteConfirmation() }
                )

                RemoveParticipantConfirmationDialog(
                    isVisible = uiState.showRemoveParticipantDialog,
                    participantName = uiState.participantToRemove?.name ?: "este participante",
                    isRemoving = uiState.removingParticipantUserId != null,
                    onConfirm = { viewModel.removeSelectedParticipant() },
                    onDismiss = { viewModel.dismissRemoveParticipantDialog() }
                )
            }
        }
    }
}
