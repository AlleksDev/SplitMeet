package com.coditos.splitmeet.features.product.di

import com.coditos.splitmeet.features.product.data.repositories.ProductRepositoryImpl
import com.coditos.splitmeet.features.product.domain.repositories.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductRepositoryModule {
    @Binds
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository
}
