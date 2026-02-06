package com.coditos.splitmeet.features.product.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.features.product.domain.entities.Product
import com.coditos.splitmeet.features.product.domain.usecases.AddOutingItemUseCase
import com.coditos.splitmeet.features.product.domain.usecases.CreateProductUseCase
import com.coditos.splitmeet.features.product.domain.usecases.DeleteOutingItemUseCase
import com.coditos.splitmeet.features.product.domain.usecases.GetOutingProductsUseCase
import com.coditos.splitmeet.features.product.domain.usecases.GetProductsByCategoryUseCase
import com.coditos.splitmeet.features.product.presentation.screens.AddProductsUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddProductsViewModel(
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
    private val getOutingProductsUseCase: GetOutingProductsUseCase,
    private val addOutingItemUseCase: AddOutingItemUseCase,
    private val createProductUseCase: CreateProductUseCase,
    private val deleteOutingItemUseCase: DeleteOutingItemUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddProductsUiState())
    val uiState = _uiState.asStateFlow()

    fun initialize(outingId: Long, categoryId: Long, categoryName: String) {
        _uiState.update { 
            it.copy(
                outingId = outingId,
                categoryId = categoryId,
                categoryName = categoryName,
                isLoading = true
            )
        }
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Load outing products
            loadOutingProducts()
            
            // Load catalog products
            loadCatalogProducts()
            
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun loadOutingProducts() {
        val result = getOutingProductsUseCase(_uiState.value.outingId)
        Log.d("AddProductsViewModel", "Outing products result: $result")
        
        result.fold(
            onSuccess = { products ->
                _uiState.update { it.copy(outingProducts = products) }
            },
            onFailure = { error ->
                Log.e("AddProductsViewModel", "Error loading outing products", error)
            }
        )
    }

    private suspend fun loadCatalogProducts() {
        _uiState.update { it.copy(isLoadingProducts = true) }
        
        val result = getProductsByCategoryUseCase(_uiState.value.categoryId)
        Log.d("AddProductsViewModel", "Catalog products result: $result")
        
        result.fold(
            onSuccess = { products ->
                _uiState.update { it.copy(catalogProducts = products, isLoadingProducts = false) }
            },
            onFailure = { error ->
                Log.e("AddProductsViewModel", "Error loading catalog products", error)
                _uiState.update { it.copy(isLoadingProducts = false) }
            }
        )
    }

    // Modal functions
    fun showAddProductModal(product: Product? = null) {
        _uiState.update {
            it.copy(
                showAddProductModal = true,
                selectedProduct = product,
                productName = product?.name ?: "",
                productPresentation = product?.presentation ?: "",
                productQuantity = 1,
                productPrice = product?.defaultPrice?.toString() ?: ""
            )
        }
    }

    fun hideAddProductModal() {
        _uiState.update {
            it.copy(
                showAddProductModal = false,
                selectedProduct = null,
                productName = "",
                productPresentation = "",
                productQuantity = 1,
                productPrice = "",
                error = null
            )
        }
    }

    // Form field updates
    fun onProductNameChanged(name: String) {
        _uiState.update { it.copy(productName = name) }
    }

    fun onProductPresentationChanged(presentation: String) {
        _uiState.update { it.copy(productPresentation = presentation) }
    }

    fun onProductQuantityChanged(quantity: Int) {
        if (quantity >= 1) {
            _uiState.update { it.copy(productQuantity = quantity) }
        }
    }

    fun onProductPriceChanged(price: String) {
        // Allow only valid price input
        if (price.isEmpty() || price.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
            _uiState.update { it.copy(productPrice = price) }
        }
    }

    // Add item to outing
    fun addItemToOuting() {
        val state = _uiState.value
        val price = state.productPrice.toDoubleOrNull() ?: return
        
        _uiState.update { it.copy(isAddingItem = true, error = null) }

        viewModelScope.launch {
            val result = addOutingItemUseCase(
                outingId = state.outingId,
                productId = state.selectedProduct?.id,
                customName = if (state.selectedProduct == null) state.productName else null,
                customPresentation = if (state.selectedProduct == null) state.productPresentation.ifBlank { null } else null,
                quantity = state.productQuantity,
                unitPrice = price,
                isShared = false
            )

            result.fold(
                onSuccess = { newItem ->
                    _uiState.update { 
                        it.copy(
                            isAddingItem = false,
                            outingProducts = it.outingProducts + newItem
                        ) 
                    }
                    hideAddProductModal()
                    showSuccessMessage("Producto agregado")
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            isAddingItem = false,
                            error = error.message ?: "Error al agregar producto"
                        ) 
                    }
                }
            )
        }
    }

    // Delete item from outing
    fun deleteOutingItem(itemId: Long) {
        _uiState.update { it.copy(isDeletingItem = itemId) }

        viewModelScope.launch {
            val result = deleteOutingItemUseCase(_uiState.value.outingId, itemId)

            result.fold(
                onSuccess = {
                    _uiState.update { 
                        it.copy(
                            isDeletingItem = null,
                            outingProducts = it.outingProducts.filter { p -> p.id != itemId }
                        ) 
                    }
                    showSuccessMessage("Producto eliminado")
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            isDeletingItem = null,
                            error = error.message ?: "Error al eliminar producto"
                        ) 
                    }
                }
            )
        }
    }

    // Success message functions
    private fun showSuccessMessage(message: String) {
        _uiState.update { 
            it.copy(
                successMessage = message,
                showSuccessMessage = true
            ) 
        }
        viewModelScope.launch {
            delay(3000)
            hideSuccessMessage()
        }
    }

    fun hideSuccessMessage() {
        _uiState.update { 
            it.copy(
                successMessage = null,
                showSuccessMessage = false
            ) 
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            loadOutingProducts()
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
