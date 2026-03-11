package com.coditos.splitmeet.core.di

import com.coditos.splitmeet.core.navigation.FeatureNavGraph
import com.coditos.splitmeet.features.auth.navigation.AuthNavGraph
import com.coditos.splitmeet.features.detailOuting.navigation.DetailOutingNavGraph
import com.coditos.splitmeet.features.home.navigation.HomeNavGraph
import com.coditos.splitmeet.features.manageOuting.navigation.ManageOutingNavGraph
import com.coditos.splitmeet.features.outing.navigation.OutingNavGraph
import com.coditos.splitmeet.features.product.navigation.ProductNavGraph
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    @IntoSet
    abstract fun bindAuthNavGraph(impl: AuthNavGraph): FeatureNavGraph

    @Binds
    @IntoSet
    abstract fun bindHomeNavGraph(impl: HomeNavGraph): FeatureNavGraph

    @Binds
    @IntoSet
    abstract fun bindOutingNavGraph(impl: OutingNavGraph): FeatureNavGraph

    @Binds
    @IntoSet
    abstract fun bindManageOutingNavGraph(impl: ManageOutingNavGraph): FeatureNavGraph

    @Binds
    @IntoSet
    abstract fun bindDetailOutingNavGraph(impl: DetailOutingNavGraph): FeatureNavGraph

    @Binds
    @IntoSet
    abstract fun bindProductNavGraph(impl: ProductNavGraph): FeatureNavGraph
}
