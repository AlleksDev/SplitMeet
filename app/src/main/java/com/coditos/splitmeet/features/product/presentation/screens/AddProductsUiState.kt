package com.coditos.splitmeet.features.product.presentation.screens

import com.coditos.splitmeet.features.product.domain.entities.OutingProduct
import com.coditos.splitmeet.features.product.domain.entities.Product

data class AddProductsUiState(
    // Loading states
    val isLoading: Boolean = false,
    val isLoadingProducts: Boolean = false,
    val isAddingItem: Boolean = false,
    val isCreatingProduct: Boolean = false,
    val isDeletingItem: Long? = null,
    
    // Data
    val outingId: Long = 0,
    val categoryId: Long = 0,
    val categoryName: String = "",
    val outingProducts: List<OutingProduct> = emptyList(),
    val catalogProducts: List<Product> = emptyList(),
    
    // Modal state
    val showAddProductModal: Boolean = false,
    val selectedProduct: Product? = null,
    
    // Form fields for adding item
    val productName: String = "",
    val productPresentation: String = "",
    val productQuantity: Int = 1,
    val productPrice: String = "",
    
    // Error state
    val error: String? = null,
    
    // Success message
    val successMessage: String? = null,
    val showSuccessMessage: Boolean = false
) {
    val total: Double get() = outingProducts.sumOf { it.subtotal }
    
    val canAddItem: Boolean get() = 
        (selectedProduct != null || productName.isNotBlank()) && 
        productQuantity > 0 && 
        productPrice.toDoubleOrNull() != null && 
        productPrice.toDoubleOrNull()!! > 0
}
