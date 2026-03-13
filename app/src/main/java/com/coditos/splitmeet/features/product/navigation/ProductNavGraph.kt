package com.coditos.splitmeet.features.product.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.coditos.splitmeet.core.navigation.AddProducts
import com.coditos.splitmeet.core.navigation.FeatureNavGraph
import com.coditos.splitmeet.core.navigation.OutingDetail
import com.coditos.splitmeet.features.product.presentation.screens.AddProductsScreen
import com.coditos.splitmeet.features.product.presentation.viewmodels.AddProductsViewModel
import javax.inject.Inject

class ProductNavGraph @Inject constructor() : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<AddProducts> { backStackEntry ->
            val addProducts: AddProducts = backStackEntry.toRoute()
            val viewModel: AddProductsViewModel = hiltViewModel()
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
