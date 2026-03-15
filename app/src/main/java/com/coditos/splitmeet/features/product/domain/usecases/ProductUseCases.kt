package com.coditos.splitmeet.features.product.domain.usecases

data class ProductUseCases(
    val addOutingItem: AddOutingItemUseCase,
    val createProduct: CreateProductUseCase,
    val deleteOutingItem: DeleteOutingItemUseCase,
    val getOutingProducts: GetOutingProductsUseCase,
    val getProductsByCategory: GetProductsByCategoryUseCase
)