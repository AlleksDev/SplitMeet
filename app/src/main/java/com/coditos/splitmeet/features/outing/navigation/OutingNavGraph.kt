package com.coditos.splitmeet.features.outing.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.coditos.splitmeet.core.navigation.CreateOuting
import com.coditos.splitmeet.core.navigation.FeatureNavGraph
import com.coditos.splitmeet.core.navigation.OutingDetail
import com.coditos.splitmeet.features.outing.di.OutingModule
import com.coditos.splitmeet.features.outing.presentation.screens.CreateOutingScreen
import com.coditos.splitmeet.features.outing.presentation.viewmodels.OutingViewModel

class OutingNavGraph(
    private val outingModule: OutingModule
) : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<CreateOuting> {
            val viewModel: OutingViewModel = viewModel(
                factory = outingModule.provideOutingViewModelFactory()
            )
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
