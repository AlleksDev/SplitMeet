package com.coditos.splitmeet.features.product.di

import com.coditos.splitmeet.features.product.domain.repositories.ProductRepository
import com.coditos.splitmeet.features.product.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductUseCaseModule {

    @Provides
    @Singleton
    fun provideAddOutingItemUseCase(repository: ProductRepository): AddOutingItemUseCase {
        return AddOutingItemUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCreateProductUseCase(repository: ProductRepository): CreateProductUseCase {
        return CreateProductUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteOutingItemUseCase(repository: ProductRepository): DeleteOutingItemUseCase {
        return DeleteOutingItemUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetOutingProductsUseCase(repository: ProductRepository): GetOutingProductsUseCase {
        return GetOutingProductsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetProductsByCategoryUseCase(repository: ProductRepository): GetProductsByCategoryUseCase {
        return GetProductsByCategoryUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideProductUseCases(
        addOutingItem: AddOutingItemUseCase,
        createProduct: CreateProductUseCase,
        deleteOutingItem: DeleteOutingItemUseCase,
        getOutingProducts: GetOutingProductsUseCase,
        getProductsByCategory: GetProductsByCategoryUseCase
    ): ProductUseCases {
        return ProductUseCases(
            addOutingItem = addOutingItem,
            createProduct = createProduct,
            deleteOutingItem = deleteOutingItem,
            getOutingProducts = getOutingProducts,
            getProductsByCategory = getProductsByCategory
        )
    }
}