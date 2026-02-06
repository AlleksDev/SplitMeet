package com.coditos.splitmeet.features.product.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.coditos.splitmeet.core.navigation.AddProducts
import com.coditos.splitmeet.core.navigation.FeatureNavGraph
import com.coditos.splitmeet.core.navigation.OutingDetail
import com.coditos.splitmeet.features.product.di.ProductModule
import com.coditos.splitmeet.features.product.presentation.screens.AddProductsScreen
import com.coditos.splitmeet.features.product.presentation.viewmodels.AddProductsViewModel

class ProductNavGraph(
    private val productModule: ProductModule
) : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<AddProducts> { backStackEntry ->
            val addProducts: AddProducts = backStackEntry.toRoute()
            val viewModel: AddProductsViewModel = viewModel(
                factory = productModule.provideAddProductsViewModelFactory()
            )
            AddProductsScreen(
                outingId = addProducts.outingId,
                categoryId = addProducts.categoryId,
                categoryName = addProducts.categoryName,
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onFinish = {
                    // Navigate back to outing detail
                    navController.navigate(OutingDetail(addProducts.outingId)) {
                        popUpTo(AddProducts(addProducts.outingId, addProducts.categoryId, addProducts.categoryName)) { 
                            inclusive = true 
                        }
                    }
                }
            )
        }
    }
}
