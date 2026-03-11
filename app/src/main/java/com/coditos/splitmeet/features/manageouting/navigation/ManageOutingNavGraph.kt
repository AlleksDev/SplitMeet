package com.coditos.splitmeet.features.manageOuting.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.coditos.splitmeet.core.navigation.CreateOuting
import com.coditos.splitmeet.core.navigation.FeatureNavGraph
import com.coditos.splitmeet.core.navigation.OutingDetail
import com.coditos.splitmeet.features.manageOuting.presentation.screens.CreateOutingScreen
import com.coditos.splitmeet.features.manageOuting.presentation.viewmodels.ManageOutingViewModel

class ManageOutingNavGraph : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<CreateOuting> {
            val viewModel: ManageOutingViewModel = hiltViewModel()
            CreateOutingScreen(
                viewModel = viewModel,
                onOutingCreated = { outingId ->
                    navController.navigate(OutingDetail(outingId)) {
                        popUpTo(CreateOuting) { inclusive = true }
                    }
                }
            )
        }
    }
}
