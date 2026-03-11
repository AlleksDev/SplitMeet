package com.coditos.splitmeet.features.detailOuting.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.coditos.splitmeet.core.navigation.AddProducts
import com.coditos.splitmeet.core.navigation.FeatureNavGraph
import com.coditos.splitmeet.core.navigation.Home
import com.coditos.splitmeet.core.navigation.OutingDetail
import com.coditos.splitmeet.features.detailOuting.presentation.screens.DetailOutingScreen
import com.coditos.splitmeet.features.detailOuting.presentation.viewmodels.DetailOutingViewModel
import javax.inject.Inject

class DetailOutingNavGraph @Inject constructor() : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<OutingDetail> { backStackEntry ->
            val outingDetail: OutingDetail = backStackEntry.toRoute()
            val viewModel: DetailOutingViewModel = hiltViewModel()
            DetailOutingScreen(
                outingId = outingDetail.outingId,
                viewModel = viewModel,
                onNavigateBack = {
                    navController.navigate(Home) {
                        popUpTo(OutingDetail(outingDetail.outingId)) { inclusive = true }
                    }
                },
                onNavigateToAddProducts = { id, categoryId, categoryName ->
                    navController.navigate(AddProducts(id, categoryId, categoryName))
                }
            )
        }
    }
}
