package com.coditos.splitmeet.features.home.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.coditos.splitmeet.core.navigation.CreateOuting
import com.coditos.splitmeet.core.navigation.FeatureNavGraph
import com.coditos.splitmeet.core.navigation.Home
import com.coditos.splitmeet.core.navigation.OutingDetail
import com.coditos.splitmeet.features.home.di.HomeModule
import com.coditos.splitmeet.features.home.presentation.screens.HomeScreen
import com.coditos.splitmeet.features.home.presentation.viewmodels.HomeViewModel

class HomeNavGraph(
    private val homeModule: HomeModule
) : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Home> {
            val viewModel : HomeViewModel = viewModel(
                factory = homeModule.provideHomeViewModelFactory()
            )
            HomeScreen(
                viewModel = viewModel,
                onNavigateToCreateOuting = {
                    navController.navigate(CreateOuting)
                },
                onNavigateToOutingDetail = { outingId ->
                    navController.navigate(OutingDetail(outingId))
                }
            )
        }
    }
}