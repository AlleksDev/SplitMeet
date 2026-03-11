package com.coditos.splitmeet.features.home.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.coditos.splitmeet.core.navigation.CreateOuting
import com.coditos.splitmeet.core.navigation.FeatureNavGraph
import com.coditos.splitmeet.core.navigation.Home
import com.coditos.splitmeet.core.navigation.OutingDetail
import com.coditos.splitmeet.features.home.presentation.screens.HomeScreen
import com.coditos.splitmeet.features.home.presentation.viewmodels.HomeViewModel
import javax.inject.Inject

class HomeNavGraph @Inject constructor() : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Home> {
            val viewModel : HomeViewModel = hiltViewModel()
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