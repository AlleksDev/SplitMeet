package com.coditos.splitmeet.features.outing.presentation.screens

import com.coditos.splitmeet.features.outing.domain.entities.Category
import com.coditos.splitmeet.features.group.domain.entities.Group
import com.coditos.splitmeet.features.outing.domain.entities.SplitType

data class CreateOutingUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val createdOutingId: Long? = null,
    val error: String? = null,
    
    // Form fields
    val name: String = "",
    val description: String = "",
    val selectedDate: String = "",
    val selectedCategory: Category? = null,
    val selectedSplitType: SplitType? = null,
    
    // Dropdown data
    val categories: List<Category> = emptyList(),
    val splitTypes: List<SplitType> = SplitType.getAll(),
    val groups: List<Group> = emptyList(),
    val selectedGroup: Group? = null,
    val isCategoriesLoading: Boolean = false,
    val isGroupsLoading: Boolean = false,
    
    // Validation
    val nameError: String? = null,
    val dateError: String? = null,
    val categoryError: String? = null,
    val splitTypeError: String? = null,
    val groupError: String? = null
) {
    val isFormValid: Boolean
        get() = name.isNotBlank() && 
                selectedDate.isNotBlank() && 
                selectedCategory != null && 
                selectedSplitType != null
}
