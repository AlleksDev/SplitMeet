package com.coditos.splitmeet.features.product.di

import com.coditos.splitmeet.core.di.AppContainer
import com.coditos.splitmeet.features.product.data.repositories.ProductRepositoryImpl
import com.coditos.splitmeet.features.product.domain.repositories.ProductRepository
import com.coditos.splitmeet.features.product.domain.usecases.AddOutingItemUseCase
import com.coditos.splitmeet.features.product.domain.usecases.CreateProductUseCase
import com.coditos.splitmeet.features.product.domain.usecases.DeleteOutingItemUseCase
import com.coditos.splitmeet.features.product.domain.usecases.GetOutingProductsUseCase
import com.coditos.splitmeet.features.product.domain.usecases.GetProductsByCategoryUseCase
import com.coditos.splitmeet.features.product.presentation.viewmodels.AddProductsViewModelFactory

class ProductModule(
    private val appContainer: AppContainer
) {
    private val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl(appContainer.splitMeetApi)
    }

    private fun provideGetProductsByCategoryUseCase(): GetProductsByCategoryUseCase {
        return GetProductsByCategoryUseCase(productRepository)
    }

    private fun provideGetOutingProductsUseCase(): GetOutingProductsUseCase {
        return GetOutingProductsUseCase(productRepository)
    }

    private fun provideAddOutingItemUseCase(): AddOutingItemUseCase {
        return AddOutingItemUseCase(productRepository)
    }

    private fun provideCreateProductUseCase(): CreateProductUseCase {
        return CreateProductUseCase(productRepository)
    }

    private fun provideDeleteOutingItemUseCase(): DeleteOutingItemUseCase {
        return DeleteOutingItemUseCase(productRepository)
    }

    fun provideAddProductsViewModelFactory(): AddProductsViewModelFactory {
        return AddProductsViewModelFactory(
            getProductsByCategoryUseCase = provideGetProductsByCategoryUseCase(),
            getOutingProductsUseCase = provideGetOutingProductsUseCase(),
            addOutingItemUseCase = provideAddOutingItemUseCase(),
            createProductUseCase = provideCreateProductUseCase(),
            deleteOutingItemUseCase = provideDeleteOutingItemUseCase()
        )
    }
}
