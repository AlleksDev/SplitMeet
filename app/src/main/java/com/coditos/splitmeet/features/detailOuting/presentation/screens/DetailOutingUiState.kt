package com.coditos.splitmeet.features.detailOuting.presentation.screens

import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingDetail
import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingItem
import com.coditos.splitmeet.features.detailOuting.domain.entities.Participant
import com.coditos.splitmeet.features.detailOuting.domain.entities.SearchUser
import com.coditos.splitmeet.features.outing.domain.entities.Category

data class DetailOutingUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    
    // Outing detail
    val outingDetail: OutingDetail? = null,
    
    // Items/Products
    val items: List<OutingItem> = emptyList(),
    val isItemsLoading: Boolean = false,
    
    // Participants
    val participants: List<Participant> = emptyList(),
    val isParticipantsLoading: Boolean = false,
    
    // Add participant modal
    val showAddParticipantModal: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<SearchUser> = emptyList(),
    val isSearching: Boolean = false,
    val addingParticipantId: Long? = null,
    val addParticipantError: String? = null,
    
    // Edit outing modal
    val showEditModal: Boolean = false,
    val editName: String = "",
    val editDescription: String = "",
    val editCategoryId: Long? = null,
    val editOutingDate: String = "",
    val editSplitType: String = "equal",
    val isUpdating: Boolean = false,
    val categories: List<Category> = emptyList(),
    
    // Delete confirmation dialog
    val showDeleteConfirmation: Boolean = false,
    val isDeleting: Boolean = false,
    
    // Success/Error messages
    val successMessage: String? = null,
    val showSuccessMessage: Boolean = false
) {
    val totalItems: Double
        get() = items.sumOf { it.subtotal }
    
    val amountPerPerson: Double
        get() {
            val participantCount = participants.size.coerceAtLeast(1)
            return if (outingDetail?.totalAmount != null && outingDetail.totalAmount > 0) {
                outingDetail.totalAmount / participantCount
            } else {
                totalItems / participantCount
            }
        }
    
    val paidParticipantsCount: Int
        get() = participants.count { it.isPaid }
    
    val hasItems: Boolean
        get() = items.isNotEmpty()
    
    val displayedItems: List<OutingItem>
        get() = items.take(3)
    
    val hasMoreItems: Boolean
        get() = items.size > 3
}
