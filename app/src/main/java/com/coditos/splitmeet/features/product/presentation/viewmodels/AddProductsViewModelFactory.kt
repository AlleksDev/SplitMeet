package com.coditos.splitmeet.features.product.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coditos.splitmeet.features.product.domain.usecases.AddOutingItemUseCase
import com.coditos.splitmeet.features.product.domain.usecases.CreateProductUseCase
import com.coditos.splitmeet.features.product.domain.usecases.DeleteOutingItemUseCase
import com.coditos.splitmeet.features.product.domain.usecases.GetOutingProductsUseCase
import com.coditos.splitmeet.features.product.domain.usecases.GetProductsByCategoryUseCase

class AddProductsViewModelFactory(
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
    private val getOutingProductsUseCase: GetOutingProductsUseCase,
    private val addOutingItemUseCase: AddOutingItemUseCase,
    private val createProductUseCase: CreateProductUseCase,
    private val deleteOutingItemUseCase: DeleteOutingItemUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddProductsViewModel::class.java)) {
            return AddProductsViewModel(
                getProductsByCategoryUseCase = getProductsByCategoryUseCase,
                getOutingProductsUseCase = getOutingProductsUseCase,
                addOutingItemUseCase = addOutingItemUseCase,
                createProductUseCase = createProductUseCase,
                deleteOutingItemUseCase = deleteOutingItemUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
